# Eclipse-OneClick-Import
Eclipse One Click Import Plugin

forked from srasul/Eclipse-Bulk-Import project and simplified.

Purpose:
I've a project consist of a lot of maven sub modules / projects. 
Instead of importing all of them to workspace i've imported it as one maven project.
When i need to work on a submodule/project i select the folder of the submodule/project then press the import icon that this plugin provides.
Then the project imported to my workspace. After i completed the work i remove the project(s) imported.

If the folder is not a eclipse project first i run "mvn eclipse:eclipse" command for this folder then import it.
