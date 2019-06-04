# MordheimCOTDDBPatch
DB patch for Mordheim city of the damned

There's 2 main's methods.
The first one is a program that will open a local mordheim DB file and update it based on the SQL commands that are within sqlCommands.txt
The second one is the one used to produce sql commands (the program that creates the initial sqlCommands.txt)

Name of the files are self-explanatory

the compiled runnable jar is the first one.

The mordheim database is inside steam filesystem : mordheim/mordheim_data/StreamingAssets/database/mordheim (the file is the database)

1) BACKUP your original database : if you do not, you'll have to reacquire it through steam "check game files integrity"
2) Simply put the runnable jar and its sqlCommands.txt in the same directory as the database file, execute it, Press "Patch" and the sl update will take place. If everything is going right, it will says "done!".



Once patched, you can safely remove the jar executable and the sqlCommands.txt from the /database folder.
Notes : Using it twice on the same db will fail
Notes : Uses this at your own RISKS. BACKUP your SAVES and engage in MP session with a modified version of the game at your own risks.
Notes : I am not affiliated with Rogue Factor (the studio who made Mordheim : COTD) nor GameWorkshop. Neither RF or GW endors or support modding.

