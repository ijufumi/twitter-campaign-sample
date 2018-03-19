FROM mysql:5.7

MAINTAINER root

#RUN groupmod -g 1000 mysql && usermod -u 1000 -g 1000 mysql
COPY ./my.cnf /etc/mysql/conf.d/my.cnf
RUN chmod 644 /etc/mysql/conf.d/my.cnf
