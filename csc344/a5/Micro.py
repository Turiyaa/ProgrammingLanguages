import os
if __name__ == '__main__':
    fileDir = input("Enter file Directory: ")
    for fileName in os.listdir(fileDir):
        if fileName:
            os.system('wc -l ' + fileName)
