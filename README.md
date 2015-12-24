# Filerenamer
A program that can rename multiple files in sequence
How to use:
  First click 'Select Files' button, a browsing window will open, choose all files you want to rename
  Then write the filename you want on the right of 'new name'
  Write the number you want to start with beside 'starting#'
  Toggle 'Keep Extension' if u want to keep existing extension
  Click 'Rename' button

How to make filename:
  eg. Prefix#Suffix.Extension
    this will rename the files into Prefix01Suffix.Extension to Prefix99Suffix.Extension (assuming 99 files)
    or even more/less, depending on how many files you choose
    if you choose a starting# of 100, then the files will be named Prefix100Suffix.Extension to Prefix198Suffix.Extension
    
Problems:
  Filename currently too uniformed
  There is currently no way to remove leading 0s, eg change 001 to 1

Note:
  Save your files first before using to avoid loss of file
