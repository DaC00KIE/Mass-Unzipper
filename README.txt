Ok so how do you use this thingy.
Simple, you just edit the config to fit your target folders and stuff, then you run the .bat file

Expected output:

All files unzipped in the same folder the .zip file is located. For those that the program have failed to unzip, it will rename the folder with an [UNZIP] prefix to show that you need to manually unzip the file. This is because the java program cant unzip certain compressed files tht were compressed in a different way [I guess Macs affect this in a way], so manually unzip em and they should be fine.


Guide on how to fill in the config.txt:

Target Folder Path [I SUGGEST U MAKE A NEW FOLDER AND PUT THE .ZIP IN IT]
^ this is where you designate the target folder that you want to unzip from.
The mass unzipper will look for ALL .zip files tht have the word "lab" in it and then unzips its contents into the same folder [it wont make a new folder].
You get this by going into the folder where the downloaded .zip file is located, then copy the folder path onto "Target Folder Path: <this part>"

Check all students
^ this is solely for if you want to download a few at a time, set it to true then it would sift through for folders that have the desired NIM on them with a .contains function. If you want to unzip all of em at once just set it to true, if not then set it to false.

Students to check
^ this is what the program will check to unzip which folders that contain the name. From the sheets you can just copas the NIMs of the students you want to check and paste it,, make sure each NIM is on a new line. If the Check all students is set to true, this part is negligible 