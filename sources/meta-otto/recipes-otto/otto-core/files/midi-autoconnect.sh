OTTO_PORT="0"
OTTO_ID="128:0" # Hardcoded

get_midi_ports() {
  # sed copies the client line in front of each port line
  aconnect -i \
    | sed -n "/^client/ h; /^ / {x;P;x;P}" \
    | while read client_line; do
        read port_line;
        client_id=$(echo $client_line | cut -d: -f1 | cut -d' ' -f2)
        port_id=$(echo $port_line | cut -d' ' -f1)

        # Skip these clients
        echo "$client_line" | grep -q "'System'" && continue
        echo "$client_line" | grep -q "'OTTO'" && continue
        echo "$client_line" | grep -q "'Midi Through'" && continue
        
        echo "$client_id:$port_id"
      done
}

get_midi_ports | while read port; do
  aconnect "$port" "$OTTO_ID"
done
 
