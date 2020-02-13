#!/usr/bin/env python
# -*- coding: utf-8 -*-

from fabric import Connection
from invoke import task
from invoke import run as local
"""
fab deploy
fab msg
"""

''' 打印颜色文本 '''
class Color:

    @classmethod
    def wrap_colour(cls, colour, *args):
        for a in args:
            print(colour + '{}'.format(a) + '\033[0m')

    @classmethod
    def green(cls, *args):
        return cls.wrap_colour('\033[92m', *args)

    @classmethod
    def red(cls, *args):
        return cls.wrap_colour('\033[91m', *args)



''' 服务器部署 '''
@task
def deploy(c):

    ''' 目标服务器 '''

    hosts = '111.200.216.79:1902'
    user = 'busadmin'
    password = '0R6EZ5SJ'

    connection = Connection(host=hosts, user=user, connect_kwargs={'password': password})

    remote_dir = '/home/busadmin/data/server'
    backup_dir = '/home/busadmin/data/server/bak'
    local_file = './be-api/target/be-api-1.0-SNAPSHOT.jar'
    jar = 'be-api-1.0-SNAPSHOT.jar'
    pid = 'pid.txt'
    Color.green('执行打包')
    local('mvn package -Dmaven.test.skip=true')

    Color.green('部署web-server...')
    with connection.cd(remote_dir):

        Color.red('停止远程服务')
        try:
            connection.run('sh stop.sh')
        except Exception, e:
            pass

        Color.red('移动远程服务器的文件至 backup 目录')
        try:
            connection.run('mv %s %s/%s' % (jar, backup_dir, jar))
        except Exception, e:
            pass

        Color.green('上传jar文件至远程服务器')
        connection.put(local_file, remote_dir)

        Color.green('启动远程服务')
        connection.run('sh -x start.sh && sleep 2')
        connection.run('ps -ef | grep %s' % jar)
        Color.green('部署成功！')


''' 消息服务器部署 '''
@task
def msg(c):

    ''' 目标服务器 '''

    hosts = '111.200.216.79:1902'
    user = 'busadmin'
    password = '0R6EZ5SJ'

    connection = Connection(host=hosts, user=user, connect_kwargs={'password': password})

    remote_dir = '/home/busadmin/data/msg'
    backup_dir = '/home/busadmin/data/msg/bak'
    local_file = './be-msg/target/be-msg-1.0-SNAPSHOT.jar'
    jar = 'be-msg-1.0-SNAPSHOT.jar'
    pid = 'pid.txt'
    Color.green('执行打包')
    local('mvn package -Dmaven.test.skip=true')

    Color.green('部署msg-server...')
    with connection.cd(remote_dir):

        Color.red('停止远程服务')
        try:
            connection.run('sh stop.sh')
        except Exception, e:
            pass

        Color.red('移动远程服务器的文件至 backup 目录')
        try:
            connection.run('mv %s %s/%s' % (jar, backup_dir, jar))
        except Exception, e:
            pass

        Color.green('上传jar文件至远程服务器')
        connection.put(local_file, remote_dir)

        Color.green('启动远程服务')
        connection.run('sh -x start.sh && sleep 2')
        connection.run('ps -ef | grep %s' % jar)
        Color.green('部署成功！')


''' 服务器部署 '''
@task
def apiDeploy(c):
    ''' 目标服务器 '''
    hosts = '121.36.11.214:22'
    user = 'root'
    password = 'Zyad@123'

    connection = Connection(host=hosts, user=user, connect_kwargs={'password': password})

    ''' 部署文件信息 '''
    remote_dir = '/data/server'
    backup_dir = '/data/server/bak'
    local_file = './be-api/target/be-api-1.0-SNAPSHOT.jar'
    jar = 'be-api-1.0-SNAPSHOT.jar'
    pid = 'pid.txt'

    Color.green('执行打包')
    local('mvn package -Dmaven.test.skip=true')

    Color.green('部署web-server...')
    with connection.cd(remote_dir):

        Color.red('停止远程服务')
        try:
            connection.run('sh stop.sh')
        except Exception, e:
            pass

        Color.red('移动远程服务器的文件至 backup 目录')
        try:
            connection.run('mv %s %s/%s' % (jar, backup_dir, jar))
        except Exception, e:
            pass

        Color.green('上传jar文件至远程服务器')
        connection.put(local_file, remote_dir)

        Color.green('启动远程服务')
        connection.run('sh -x start.sh && sleep 2')
        connection.run('ps -ef | grep %s' % jar)
        Color.green('部署成功！')

''' 消息服务器部署 '''
@task
def msgDeploy(c):
    ''' 目标服务器 '''
    hosts = '121.36.11.214:22'
    user = 'root'
    password = 'Zyad@123'

    connection = Connection(host=hosts, user=user, connect_kwargs={'password': password})

    ''' 部署文件信息 '''
    remote_dir = '/data/msg'
    backup_dir = '/data/msg/bak'
    local_file = './be-msg/target/be-msg-1.0-SNAPSHOT.jar'
    jar = 'be-msg-1.0-SNAPSHOT.jar'
    pid = 'pid.txt'


    Color.green('执行打包')
    local('mvn package -Dmaven.test.skip=true')

    Color.green('部署msg-server...')
    with connection.cd(remote_dir):

        Color.red('停止远程服务')
        try:
            connection.run('sh stop.sh')
        except Exception, e:
            pass

        Color.red('移动远程服务器的文件至 backup 目录')
        try:
            connection.run('mv %s %s/%s' % (jar, backup_dir, jar))
        except Exception, e:
            pass

        Color.green('上传jar文件至远程服务器')
        connection.put(local_file, remote_dir)

        Color.green('启动远程服务')
        connection.run('sh -x start.sh && sleep 2')
        connection.run('ps -ef | grep %s' % jar)
        Color.green('部署成功！')
