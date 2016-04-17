#!/bin/bash
/sbin/stop tdfw > /dev/null 2>&1 || /bin/true

/bin/echo "Removing portal nginx config.."
/bin/rm -rf /etc/nginx/sites-enabled/tdfw || /bin/true
/bin/rm -rf /etc/nginx/sites-available/tdfw  || /bin/true
/bin/rm -rf /opt/tdfw/logs  || /bin/true
/usr/sbin/service nginx reload || /bin/true


