BEGIN {
    # No client when starting
    client = "none";
    # Set blacklist in keys
    blacklist["0"] = "a"
    blacklist["1"] = "a"
    blacklist[OTTO_CLIENT] = "a"
}

/^client ([0-9]+)/ {
  # found a new client
  match($0, /^client ([0-9]+):/, matches);
  client = substr($0, 
                  matches[1, "start"], 
                  matches[1, "length"]);
}

/^\s+([0-9]+) / {
  # found a new port
  match($0, /^\s+([0-9]+) /, matches);
  port = substr($0, 
                  matches[1, "start"], 
                  matches[1, "length"]);
  # Store the results in an array
  id = client ":" port;
  ports[id] = client;
}

END {
    # Print ports
    for (port in ports) {
        client = ports[port]
        if (!(client in blacklist)) {
          system("echo " port);
        }
    }
}