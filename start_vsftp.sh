docker run -itd --privileged -e FTP_USER=vsftp -e FTP_PASS=656803  --name=vsftp -v /root/data:/home/vsftp -p 20:20 -p 21:21 -p 21100-21110:21100-21110   spikebob/vsftp:v1.0.2  /usr/sbin/init
docker exec -it vsftp  /bin/bash
