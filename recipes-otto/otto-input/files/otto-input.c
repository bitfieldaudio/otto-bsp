#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <signal.h>
#include <assert.h>
#include <sys/ioctl.h>
#include <linux/i2c.h>
#include <linux/i2c-dev.h>
#include <linux/input.h>
#include <linux/uinput.h>

static int is_signaled = 0;	/* Exit program if signaled */
static int i2c_fd = -1;		/* Open /dev/i2c-1 device */
static int f_debug = 0;		/* True to print debug messages */

enum classic_pots  {RX,RY,LX,LY,LT,RT,classic_pots_MAX = RT};
enum classic_buttons {BDR,BDL,BDU,BDD,BLT,BRT,BM,BH,BP,BZL,BZR,BA,BB,BX,BY,classic_buttons_MAX = BY};
unsigned classic_button_map[classic_buttons_MAX+1] = {KEY_A,KEY_B,KEY_C,KEY_D,KEY_E,KEY_F,KEY_G,KEY_H,KEY_I,KEY_J,KEY_K,KEY_L,KEY_M,KEY_N,KEY_O};
enum postkey {PKEY_UP, PKEY_DOWN};

const char * const classic_button_names[classic_buttons_MAX+1] = {"BDR","BDL","BDU","BDD","BLT","BRT","BM","BH","BP","BZL","BZR","BA","BB","BX","BY"};
const char * const classic_pot_names[classic_pots_MAX + 1] = {"RX","RY","LX","LY","LT","RT"};

typedef struct {
	unsigned char P[6];	 	//Pot Array
	unsigned char B[15]; 	//Button Array
	unsigned char raw[6];	//Raw received data
} classic_s;

static void
timed_wait(long sec,long usec,long early_usec) {
    fd_set mt;
    struct timeval timeout;
    int rc;

    FD_ZERO(&mt);
    timeout.tv_sec = sec;
    timeout.tv_usec = usec;
    do  {
        rc = select(0,&mt,&mt,&mt,&timeout);
        if ( !timeout.tv_sec && timeout.tv_usec < early_usec )
            return;     /* Wait is good enough, exit */
    } while ( rc < 0 && timeout.tv_sec && timeout.tv_usec );
}

static void
copy_classic_s(classic_s *now, classic_s *last){
	//copy current data over to last
	int n = sizeof(now->raw) / sizeof(now->raw[0]);
	memcpy(last->raw, now->raw, n);
	
	n = sizeof(now->P) / sizeof(now->P[0]);
	memcpy(last->P, now->P, n);
	
	n = sizeof(now->B) / sizeof(now->B[0]);
	memcpy(last->B, now->B, n);
}

	
/*
 * Open I2C bus and check capabilities :
 */
static void
i2c_init(const char *node) {
	unsigned long i2c_funcs = 0;	/* Support flags */
	int rc;

	i2c_fd = open(node,O_RDWR);	/* Open driver /dev/i2s-1 */
	if ( i2c_fd < 0 ) {
		perror("Opening /dev/i2c-1");
		puts("Check that the i2c-dev & i2c-bcm2708 kernel modules "
		     "are loaded.");
		abort();
	}

	/*
	 * Make sure the driver supports plain I2C I/O:
	 */
	rc = ioctl(i2c_fd,I2C_FUNCS,&i2c_funcs);
	assert(rc >= 0);
	assert(i2c_funcs & I2C_FUNC_I2C);
}

/*
 * Configure the nunchuk/controller for no encryption:
 * Works with all controller types.
 */
static void
controller_init() {
	static char init_msg1[] = { 0xF0, 0x55 };
	static char init_msg2[] = { 0xFB, 0x00 };
	struct i2c_rdwr_ioctl_data msgset;
	struct i2c_msg iomsgs[1];
	int rc;

	iomsgs[0].addr = 0x52;		/* Address of Nunchuk */
	iomsgs[0].flags = 0;		/* Write */
	iomsgs[0].buf = init_msg1;	/* Nunchuk 2 byte sequence */
	iomsgs[0].len = 2;		/* 2 bytes */

	msgset.msgs = iomsgs;
	msgset.nmsgs = 1;

	rc = ioctl(i2c_fd,I2C_RDWR,&msgset);
	assert(rc == 1);

	timed_wait(0,200,0);		/* Nunchuk needs time */

	iomsgs[0].addr = 0x52;		/* Address of Nunchuk */
	iomsgs[0].flags = 0;		/* Write */
	iomsgs[0].buf = init_msg2;	/* Nunchuk 2 byte sequence */
	iomsgs[0].len = 2;		/* 2 bytes */

	msgset.msgs = iomsgs;
	msgset.nmsgs = 1;

	rc = ioctl(i2c_fd,I2C_RDWR,&msgset);
	assert(rc == 1);
	
}



/*
 * Read controller data:
 */
static int
classic_read(classic_s *data) {
	struct i2c_rdwr_ioctl_data msgset;
	struct i2c_msg iomsgs[1];
	char zero[1] = { 0x00 };	/* Written byte */
	int rc;
		
	timed_wait(0,15000,0);

	/*
	 * Write the controller register address of 0x00 :
	 */
	iomsgs[0].addr = 0x52;		/* controller address */
	iomsgs[0].flags = 0;		/* Write */
	iomsgs[0].buf = zero;		/* Sending buf */
	iomsgs[0].len = 1;		/*  bytes */

	msgset.msgs = iomsgs;
	msgset.nmsgs = 1;

	rc = ioctl(i2c_fd,I2C_RDWR,&msgset);
	if ( rc < 0 )
		return -1;		/* I/O error */

	timed_wait(0,200,0);		/* Zzzz, controller needs time */

	/*
	 * Read 6 bytes starting at 0x00 :
	 */
	iomsgs[0].addr = 0x52;			/* controller address */
	iomsgs[0].flags = I2C_M_RD;		/* Read */
	iomsgs[0].buf = (char *)data->raw;	/* Receive raw bytes here */
	iomsgs[0].len = 6;			/* 6 bytes */

	msgset.msgs = iomsgs;
	msgset.nmsgs = 1;

	rc = ioctl(i2c_fd,I2C_RDWR,&msgset);
	if ( rc < 0 )
		return -1;			/* Failed */

	data->P[RX] = (( data->raw[0] & 0xC0)  >> 3) |
				  (( data->raw[1] & 0xC0 ) >> 5) |
				  (( data->raw[2] & 0x80 ) >> 7);
	data->P[RY] = data->raw[2] & 0x1F;
	data->P[LX] = data->raw[0] & 0x3F;
	data->P[LY] = data->raw[1] & 0x3F;
	data->P[RT] = data->raw[3] & 0x1F;
	data->P[LT] = (( data->raw[2] & 0x60 ) >> 2) |
				  (( data->raw[3] & 0xE0 ) >> 5); 

	data->B[BDR] = data->raw[4] & 0x80 ? 0 : 1;
	data->B[BDD] = data->raw[4] & 0x40 ? 0 : 1;
	data->B[BLT] = data->raw[4] & 0x20 ? 0 : 1;
	data->B[BM]  = data->raw[4] & 0x10 ? 0 : 1;
	data->B[BH]  = data->raw[4] & 0x08 ? 0 : 1;
	data->B[BP]  = data->raw[4] & 0x04 ? 0 : 1;
	data->B[BRT] = data->raw[4] & 0x02 ? 0 : 1;
	
	data->B[BZL] = data->raw[5] & 0x80 ? 0 : 1;
	data->B[BB]  = data->raw[5] & 0x40 ? 0 : 1;
	data->B[BY]  = data->raw[5] & 0x20 ? 0 : 1;
	data->B[BA]  = data->raw[5] & 0x10 ? 0 : 1;
	data->B[BX]  = data->raw[5] & 0x08 ? 0 : 1;
	data->B[BZR] = data->raw[5] & 0x04 ? 0 : 1;
	data->B[BDL] = data->raw[5] & 0x02 ? 0 : 1;
	data->B[BDU] = data->raw[5] & 0x01 ? 0 : 1;
	
	return 0;
}

/*
 * Dump the classic controller data:
 */
static void
dump_data_classic(classic_s *data, classic_s *last) {
	int x;

	printf("Raw : ");
	for ( x=0; x<6; x++ )
		printf(" [%02X]",data->raw[x]);
	putchar('\n');
	printf("Last: ");
	for ( x=0; x<6; x++ )
		printf(" [%02X]",last->raw[x]);
	putchar('\n');
	printf("      data last\n");
	for (x=0; x<classic_pots_MAX+1; x++)
		printf("%3s = %04X %04X\n",classic_pot_names[x], data->P[x], last->P[x]);

	for (x=0; x<classic_buttons_MAX+1; x++)
		printf("%3s = %04X %04X\n",classic_button_names[x], data->B[x], last->B[x]);	
	
}

/*
 * Close the I2C driver :
 */
static void
i2c_close(void) {
	close(i2c_fd);
	i2c_fd = -1;
}

/*
 * Open a uinput node:
 */
static int
uinput_open(void) {
	int fd;
	struct uinput_user_dev uinp;
	int rc,n;

	fd = open("/dev/uinput",O_WRONLY|O_NONBLOCK);
	if ( fd < 0 ) {
		perror("Opening /dev/uinput");
		exit(1);
	}

	rc = ioctl(fd,UI_SET_EVBIT,EV_KEY);
	assert(!rc);
	rc = ioctl(fd,UI_SET_EVBIT,EV_REL);
	assert(!rc);

	rc = ioctl(fd,UI_SET_RELBIT,REL_X);
	assert(!rc);
	rc = ioctl(fd,UI_SET_RELBIT,REL_Y);
	assert(!rc);

	rc = ioctl(fd,UI_SET_KEYBIT,KEY_ESC);
	assert(!rc);

	ioctl(fd,UI_SET_KEYBIT,BTN_MOUSE);
	ioctl(fd,UI_SET_KEYBIT,BTN_TOUCH);
	ioctl(fd,UI_SET_KEYBIT,BTN_MOUSE);
	ioctl(fd,UI_SET_KEYBIT,BTN_LEFT);
	ioctl(fd,UI_SET_KEYBIT,BTN_MIDDLE);
	ioctl(fd,UI_SET_KEYBIT,BTN_RIGHT);
	for (n = 0; n < classic_buttons_MAX+1; n++){
		ioctl(fd,UI_SET_KEYBIT,classic_button_map[n]);
	}

	memset(&uinp,0,sizeof uinp);
	strncpy(uinp.name,"classic",UINPUT_MAX_NAME_SIZE);
	uinp.id.bustype = BUS_USB;
	uinp.id.vendor  = 0x1;
	uinp.id.product = 0x1;
	uinp.id.version = 1;

	rc = write(fd,&uinp,sizeof(uinp));
	assert(rc == sizeof(uinp));

	rc = ioctl(fd,UI_DEV_CREATE);
	assert(!rc);
	return fd;
}

/*
 * Post keystroke down and keystroke up events:
 * (unused here but available for your own experiments)
 * dir: 1 = down, 0 = up
 * enum postkey {PKEY_UP, PKEY_DOWN};
 */
static void
uinput_postkey(int fd,unsigned key, unsigned dir) {
	struct input_event ev;
	int rc;

	memset(&ev,0,sizeof(ev));
	ev.type = EV_KEY;
	ev.code = key;
	ev.value = dir;	

	rc = write(fd,&ev,sizeof(ev));
	assert(rc == sizeof(ev));
}	


/*
 * Post a synchronization point :
 */
static void
uinput_syn(int fd) {
	struct input_event ev;
	int rc;

	memset(&ev,0,sizeof(ev));
	ev.type = EV_SYN;
	ev.code = SYN_REPORT;
	ev.value = 0;
	rc = write(fd,&ev,sizeof(ev));
	assert(rc == sizeof(ev));
}

/*
 * Synthesize a button click:
 *	up_down		1=up, 0=down
 *	buttons		1=Left, 2=Middle, 4=Right
 */
static void
uinput_click(int fd,int up_down,int buttons) {
	static unsigned codes[] = { BTN_LEFT, BTN_MIDDLE, BTN_RIGHT };
	struct input_event ev;
	int x;

	memset(&ev,0,sizeof(ev));

	/*
	 * Button down or up events :
	 */
	for ( x=0; x < 3; ++x ) {
		ev.type = EV_KEY;
		ev.value = up_down;		/* Button Up or down */
		if ( buttons & (1 << x) ) {	/* Button 0, 1 or 2 */
			ev.code = codes[x];
			write(fd,&ev,sizeof(ev));
		}
	}
}

/*
 * Synthesize relative mouse movement :
 */
static void
uinput_movement(int fd,int x,int y) {
	struct input_event ev;
	int rc;

	memset(&ev,0,sizeof(ev));
	ev.type = EV_REL;
	ev.code = REL_X;
	ev.value = x;

	rc = write(fd,&ev,sizeof(ev));
	assert(rc == sizeof(ev));

	ev.code = REL_Y;
	ev.value = y;
	rc = write(fd,&ev,sizeof(ev));
	assert(rc == sizeof(ev));
}	

/*
 * Close uinput device :
 */
static void
uinput_close(int fd) {
	int rc;

	rc = ioctl(fd,UI_DEV_DESTROY);
	assert(!rc);
	close(fd);
}

/*
 * Signal handler to quit the program :
 */
static void
sigint_handler(int signo) {
	is_signaled = 1;		/* Signal to exit program */
}

/*
 * Curve the adjustment :
 */
static int
curve(int relxy) {
	int ax = abs(relxy);		/* abs(relxy) */
	int sgn = relxy < 0 ? -1 : 1;	/* sign(relxy) */
	int mv = 1;			/* Smallest step */

	if ( ax > 100 )
		mv = 10;		/* Take large steps */
	else if ( ax > 65 )
		mv = 7;
	else if ( ax > 35 )
		mv = 5;
	else if ( ax > 15 )
		mv = 2;			/* 2nd smallest step */
	return mv * sgn;
}

//Checks the RAW field against last measured values to see if a
//button was pressed
//return 0 if no change
//		 1 if changed
static int
buttonChange(classic_s *data, classic_s *last){
	if ( memcmp(&(data->raw[4]), &(last->raw[4]), 2) == 0 )
		return 0;  
	else
		return 1;
}
//Checks the RAW field against last measured for stick changes, 
// with 2 points wiggle room for changes
static int
stickChange(classic_s *data, classic_s *last){
	int n;
	
	for( n=0; n < sizeof(data->P); n++){
		if (abs( data->P[n] - last->P[n]) > 0)  return 1;
	}

	return 0;
}

/*
 * Main program :
 */
void
main(int argc,char **argv) {
	int fd, need_sync;
	classic_s cl_data, cl_last;
	int delta_count = 0, n, key;

	if ( argc > 1 && !strcmp(argv[1],"-d") )
		f_debug = 1;				/* Enable debug messages */

	(void)uinput_postkey;			/* Suppress compiler warning about unused */

	i2c_init("/dev/i2c-1");			/* Open I2C controller */
	controller_init();				/* Turn off encryption */

	signal(SIGINT,sigint_handler);	/* Trap on SIGINT */
	fd = uinput_open();				/* Open /dev/uinput */
	
	classic_read(&cl_data);   		/* Read to set initial values */
	copy_classic_s(&cl_data, &cl_last);

	while ( !is_signaled ) {
		
		copy_classic_s(&cl_data, &cl_last);
		
		while (!is_signaled & ( classic_read(&cl_data) < 0 )) {}
		
		//test for button change
		//if ( buttonChange(&cl_data, &cl_last) || (stickChange(&cl_data, &cl_last)) )
		if ( buttonChange(&cl_data, &cl_last) )
			delta_count++;
		else
			continue;
			
		if ( f_debug ){
			printf("delta: %i\n",delta_count);
			dump_data_classic(&cl_data, &cl_last);	/* Dump nunchuk data */
		}
		
		//Button logic is (dataB-lastB), if 0, no action happened. If 1, button down, if -1 button up
		for(n = 0; n < (sizeof(cl_data.B)/sizeof(cl_data.B[0])); n++){

			if (cl_data.B[n] - cl_last.B[n]) // if not 0, a key action happened
			{
				// if 1, then button down, if -1 it's a button release
				if ( (cl_data.B[n] -cl_last.B[n]) == 1 ) 
					key = PKEY_DOWN;
				else
					key = PKEY_UP;		 
				
				cl_last.B[n] = cl_data.B[n];
				uinput_postkey(fd, classic_button_map[n], key);
				uinput_syn(fd);
				
				if ( f_debug ) printf("%3s %i\n", classic_button_names[n], key);
			}
		}
		
		
		
		/*
		if ( init > 0 && !data0.stick_x && !data0.stick_y ) {
			data0 = data;		// Save initial values
			last = data;
			--init;
			continue;	
		}

		need_sync = 0;
		if ( abs(data.stick_x - data0.stick_x) > 2 
		  || abs(data.stick_y - data0.stick_y) > 2 ) {
			rel_x = curve(data.stick_x - data0.stick_x);
			rel_y = curve(data.stick_y - data0.stick_y);
			if ( rel_x || rel_y ) {
				uinput_movement(fd,rel_x,-rel_y);
				need_sync = 1;
			}
		}

		need_sync = 1;
		
		if ( need_sync )
			uinput_syn(fd);
		*/
	}

	putchar('\n');
	uinput_close(fd);
	i2c_close();

}
