 #!/bin/bash
 docker run -itd --privileged --name=spike --mount type=bind,source=/root/data/project,target=/root/data/project -p 3306:3306 -p 6379:6379 -p 1723:1723 -p 8000:8000   spikebob/spike:v1.1.4  /usr/sbin/init  /etc/rc.d/init.d/start.sh
 docker exec -it spike  /bin/bash
