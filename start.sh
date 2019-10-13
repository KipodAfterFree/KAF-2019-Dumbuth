#!/bin/bash
php /var/www/html/files/dumbuth/private/setup.php
service apache2 start
java -jar /home/server.jar