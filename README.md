# Eclipse embed plugin
### Eclipse plugin for embedded things

It is draft version now, currently work on build-flow automation for Sming and other ESP8266 stuff.

## Serial terminal 
### One of embed plugin tool

Basic functionality finished.
Ports list automatically updates on ports combo click, simple click on combo after connect device.

![Actual look](https://cloud.githubusercontent.com/assets/11439226/7324394/cf78312e-eabe-11e4-91e3-239914714e23.png "Actual look")


### TODO:
* Add history to send 
* Save monitor content to file
* Add context menu to monitor
* Send file ability
* Highlight outgoing data in half-duplex mode
* Charset selection (currently used UTF-8)
* Add view-types switch (Ansi/ASCII/Hex/Bin etc.)
* Font and monitor style configuration
* Monitor Rows/Cols configuration
* Data statistics: chars/bytes/cps
* Flow control setup
* Monitor content filters
* Search monitor content
* Capture content to file
* Default connectors pinouts help
 
#### Eclipse UI extensions
* Add item in popup menu to send any file to terminal
