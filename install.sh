#!/bin/bash
mkdir -p ~/bin/jettySSLPlugin/ && \
cp -R jettySSLPlugin/ ~/bin/jettySSLPlugin/ && \
cp -R gradleSSL ~/bin/ && \
cp -R gradleSSLDebug ~/bin/ && \
cd ~/bin/jettySSLPlugin/ && \
echo "Boot-Class-Path: `pwd`/javassist.jar" >> MANIFEST.MF && \
jar -cmf MANIFEST.MF sslagent.jar HeaderAgent.class || (echo "Setup jettySSLPlugin failed." && exit "1")
echo "Setup jettySSLPlugin finished"
