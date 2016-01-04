% jfig (3.0+) README  (08.05.2005)


A.  About
0.  System requirements
1.  Installation
2.  Running jfig 
3.  Configuration via jfig.cnf
4.  Running jfig as an applet
5.  JVM Compatibility
6.  Printing and fig2dev
7.  Windows installation 
8.  Contact



-----------------------------------------------------------------------------

A. About


jfig is a 2D graphics and diagram editor written in pure Java. 
It offers all standard drawing primitives including bezier splines,
each with attributes like colors, line and fill styles, etc.
When available on your system, jfig will use the Java2D graphics 
library for high-quality rendering.

For detailed information and downloads, visit the jfig homepage at
http://tams-www.informatik.uni-hamburg.de/applets/jfig/


The user interface of jfig aims to be compatible with "xfig", 
a very popular graphics editor on Unix and Linux workstations.
While jfig runs fine on Unix and Linux, the most common reason
to use jfig is to have an xfig-compatible graphics editor on
Windows systems, e.g. a notebook PC.  Some Windows utility files 
and a precompiled fig2dev.exe executable for Windows are available 
for separate download from the jfig homepage.

xfig and jfig are frequently used to prepare illustrations for
LaTex documents, and include special support for LaTeX text objects.
You can also preview your LaTeX text objects in jfig.

For information and details about jfig visit the jfig homepage, 
http://tams-www.informatik.uni-hamburg.de/applets/jfig/

LICENSE:  Please read the information in /jfig/doc/LICENSE.TXT
for information about jfig licensing. The editor is shareware
and you should consider to pay the shareware fee if you use
jfig regularly. Also note that some features of jfig (e.g.
PDF export) are disabled in the non-registered version.

RELEASE NOTES:  There are currently three versions of the jfig
editor: 

* jfig 1.xx is based on AWT-rendering and remains Java-1.1
  compatible. It runs with JDK/JRE 1.1.8 or higher and the
  Microsoft VM, but is no longer supported and lacks a lot
  of features compared to later versions.

* jfig 2.xx is based on Swing and Java2D, but requires 
  JDK/JRE 1.2 or higher, with JDK/JRE 1.4.x recommended.
  This version ools better and has many more functions than
  jfig 1.xx.

* jfig 3.xx is a mostly re-written version of jfig 2.xx, 
  also based on Swing and Java2D. I uses double-precision
  coordinates internally and removes several limitations
  of the previous version. Requires JDK/JRE 1.4.x or higher.

In order to get more testing for jfig 3.xx, the jfig 2.xx 
versions are no longer available for downloading from our
server.




-----------------------------------------------------------------------------

0. System requirements

In theory, jfig3 should run on all machines with a Java 1.4.x
or Java 5.x compatible Java virtual machine and enough memory. 
In practice, you will want the following:

  Pentium-III (800MHz) or better
  256 MBytes of memory
  1024x768 screen
  JDK/JRE 1.4.2 or later

Note that it is also possible to run jfig (1.xx) with older versions
of the Java virtual machine, e.g. JDK 1.1.8 or the Microsoft VM
(jview). See the instructions below.



-----------------------------------------------------------------------------

1. Download and Installation 

The jfig editor consists of about 250 individual Java classes
(parts of the program), bundled together in two archives files.
The file "jfig3.jar" contains the current version of the editor,
and "jfig3-itext.jar" contains both the editor and the itext PDF
library, while "jfig.jar" includes the old (1.xx) version.

On computers with an up-to-date Java installation (JDK 1.4+), the
easiest way to download and install jfig is to use the Java Webstart 
system; simply click on the "Download and run" button on the jfig 
webstart page to start the download and installation. Java Webstart 
will automatically check for new versions when you are online. 
Naturally, your current version of jfig will be used while you are 
offline.

Alternatively, you can download the JAR archives manually; just
follow your browser's convention to download the files in binary
transfer mode (e.g., in and Mozilla use a right click on the links, 
then select "Save Link Target As"). It is not necessary but 
recommended to create a new directory to keep your jfig archives.


-----------------------------------------------------------------------------

2. Running jfig

To start the editor as an application use the following commands:

- Java Webstart:

  Use Java Webstart to run the jfig editor. In the default Windows
  installation, Java Webstart will create program menu and desktop
  icons that you can use to start jfig like any other Windows program.

- JDK/JRE 1.4 or later:

  Assuming that JDK/JRE 1.4.x is your default Java executable, 
  you can run jfig directly from the jfig.jar archive, without 
  having to bother with CLASSPATH settings. Just open a command
  shell, go to the directory that you saved the jfig3.jar archive in,
  and type the following command:

  java -jar jfig3.jar [filename.fig]

  If you specify a filename, the editor will load the given file.


- JDK/JRE 1.3 or later:

  On Windows, you can also start the editor by double-clicking 
  the jfig3.jar file in the Windows explorer. You can also drop
  a link from jfig3.jar to your desktop or the start menu for 
  easier access.

  NOTE: unfortunately, the default installation of the JDK/JRE 
  does not correctly handle filenames with spaces in them. For
  example, trying to double click a jfig3.jar installed into
  "c:\my files\jfig3.jar" or "c:\program files\jfig\jfig3.jar"
  will not work, because of the space characters.

  See section 7) below for further information on Windows installation.


- JDK 1.1.x  or higher:
  
  set PATH=%PATH%;c:\path\to\your\JDK\bin
  set CLASSPATH=%CLASSPATH%;c:\path\to\jfig.jar

  java jfig.gui.Editor [filename.fig]

- JRE 1.1.x and JRE 1.2.x 

  set PATH=%PATH%;c:\path\to\your\JDK\bin
  set CLASSPATH=%CLASSPATH%;c:\path\to\jfig.jar

  java jfig.gui.Editor [filename.fig]

- Microsoft VM

  jview /cp c:\path\to\jfig.jar jfig.gui.Editor [filename.fig]
  



-----------------------------------------------------------------------------

3. Configuration via .jfigrc

Many properties and attribute settings in jfig can be preset from
a configuration file. The default values are read on program startup
from the "master" configuration file in the jfig3.jar archive,
called "/jfig/jfig.cnf". To adapt jfig to your preferences, start 
jfig and select the menu->options->show properties menu command.
Browse through the dialog window and change the values of properties
as you like.

At the very least, you should edit the command lines for Postscript 
export and printing if you have fig2dev available (see below).
Also, set "jfig.FIG.Version 32" or "jfig.FIG.Version 31"
to set the default output format to FIG 3.2 or FIG 3.1.

jfig will load and merge all configuration files from three different
directories, so you can have global jfig options, per-user options,
and per-directory options:

  1. the global 'jfig.cnf' file from the CLASSPATH
  2. a '.jfigrc' file in your home directory
  3. a '.jfigrc' file in your current working directory

Note that global properties may be overwritten by the home-directory
properties, which in turn may be overwritten by current-directory
settings. 

On Windows, the location of your home directory depends on the
Java virtual machine and the Windows version. For example:

Windows 95, Microsoft jview:   C:\windows\Java
Windows 95, Sun JDK 1.3:       C:\windows\system
Windows XP, Sun JDK 1.4:       C:\documents and settings\username
etc.

To check the home directory location on your system, start jfig,
select menu->options->show properties and look for the user.home
property.

The current version of jfig also allows you to write the user.home
and working.directory configuration files directly from the
show properties dialog window. Just select the corresponding "save
.jfigrc" button and afterwards edit the file.

Note: On Windows systems, it can be difficult to create new files
whose names start with a dot; depdingin on the Explorer settings.
If necessary, first create the configuration files from the show
properties dialog; once the file is there, Explorer will allow 
you to edit it.




-----------------------------------------------------------------------------

4. Running jfig as an applet

See the applet page on the jfig homepage for further information 
about the 'jfig bean' and 'jfig applet' components.




-----------------------------------------------------------------------------

5. JVM compatibility

jfig 3.xx requires JDK/JRE 1.4.2 or J2SE 5.x (JDK/JRE 1.5.x).

jfig 1.xx should run with any recent Java virtual machine that 
supports at least the Java 1.1 API specification. This includes the
Microsoft VM (jview build 3.181 or later) and all versions of the 
JDK/JRE 1.18 or later.



-----------------------------------------------------------------------------

6. Printing and fig2dev

jfig native printing should be possible on all operating systems, 
but the output quality is limited due to a variety of problems.
For example, Java 1.1 virtual machines only use a resolution of 
72dpi for printing. 

Java 2 (JDK/JRE 1.4.2 or later) should provide good printing quality 
on all printers on your system. However, you may still encounter 
problems, due to a variety of bugs in the Java print system itself.
jfig3 also supports build-in JPEG, PNG, and PPM image format export.

Postscript export and Postscript printing are available via the 
transfig package and fig2dev drivers from the xfig distribution. 
Fig2dev is available in the Public Domain but protected under the 
X11 Copyright terms. On most Unix and Linux platforms, the fig2dev
program is installed by default. For Windows, you can download a
precompiled binary of fig2dev.3.2.3 (fig2dev-windows.zip) from the 
jfig homepage.

The registered version of jfig 3.xx also supports a "pure Java"
export to PDF format via the iText class library. Please see the
additional documentation in /jfig/doc/export-fig2dev.txt for
further information.





-----------------------------------------------------------------------

7. Windows installation

On Windows systems, you may want to make jfig your default editor
for FIG files, so that double-clicking a ".fig" file directly
starts the jfig editor and opens the ".fig" file.

Unfortunately, the details on how to enable this behaviour depend
on many things: the Java version used, the Windows version,
and the installation paths used for the jfig3.jar archive and your
FIG files.  It is therefore almost impossible to give fail-save
instructions on how to enable the correct behaviour. 

The basic idea, however, is to create a mapping from the ".fig"
file extension to the Windows internal "open" command for that
type of file - which should be a command line like the following

"C:\j2sre-1.4.2\bin\javaw.exe" -jar "c:\my files\jfig3.jar" "%1"

where the first path specifies the Java virtual machine executable,
the second specifies the path to the jfig3.jar archive, and the
third part ("%1") the actual FIG file name. The quotes are needed
to support file names with spaces or special chars in them.

In most versions of Windows, you can access and edit the "open"
command for a file type from an Explorer window. Try to find the
dialog via "explorer->options->display->file types". 
Next, search for a ".fig" file type and edit the type. 
Otherwise, add a ".fig" type and enter the "open" command.
Please ask your local Windows guru on help about the details
required for your version of Windows.








-----------------------------------------------------------------------

8. Contact

Please send requests, but reports, and other feedback to the 
following e-mail address:   hendrich@informatik.uni-hamburg.de
