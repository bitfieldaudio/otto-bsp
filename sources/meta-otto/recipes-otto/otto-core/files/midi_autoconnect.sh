PARSE_PATH="/home/root/scripts/parse_aconnect.awk"
OTTO_CLIENT="128"
OTTO_PORT="0"
OTTO_ID=$OTTO_CLIENT":"$OTTO_ID
PORTS=`aconnect -i | $PARSE_PATH -v OTTO_CLIENT=$OTTO_CLIENT`

for PORT in $PORTS; do
  aconnect $PORT $OTTO_ID  
done
 