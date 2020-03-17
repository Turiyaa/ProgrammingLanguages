import os
import string


def filter_file():
    for line in file:
        if not line.__contains__('write'):
            if not line.__contains__('print'):
                if not line.startswith(('//', '#', 'import', '\n', '%%', ";")):
                    for space in line.split(" "):
                        for comma in space.split(","):
                            for op in comma.split("("):
                                for bar in op.split("|"):
                                    for ob in bar.split("["):
                                        wordSet.add(ob)


def filter_words():
    for letter in wordSet:
        for c in string.punctuation:
            letter = letter.replace(c, "")
        letter = letter.replace("\n", "")
        if not letter.isnumeric():
            identifiers.add(letter)


def create_index_page():
    indexFile.write("<h1><a href=" + linkName + ">" + summeryLink + "</a></h1>")
    indexFile.write("</html>")


if __name__ == '__main__':
    fileDir = "/Users/narayan/PycharmProjects/microproject/csc344"
    indexFile = open(fileDir + "/" + "index.html", "a")
    indexFile.write("<html> <h1 align = " + 'center' + " >Assignment 5</h1>")
    for directory in os.walk(fileDir):
        if directory:
            for dir_name in directory[1]:
                print(dir_name)
                for fileName in os.listdir(fileDir + "/" + dir_name):
                    summeryLink = "summery_" + dir_name + ".html"
                    linkName = fileDir + "/" + dir_name + "/" + summeryLink
                    f = open(linkName, "a")
                    if fileName:
                        wordSet = set()
                        identifiers = set()
                        file = open(fileDir + "/" + dir_name + "/" + fileName, "r")
                        count = os.popen('wc -l < ' + fileDir + "/" + dir_name + "/" + fileName + ' | xargs').read()
                        f.write("<html>")
                        f.write("<h1><a href=" + fileName + ">" + fileName + " " + str(count) + "</a></h1>")
                        f.write("<p>")
                        filter_file()
                        filter_words()
                        for l in identifiers:
                            f.write(l)
                            f.write(' ')
                        f.write("</p>")
                        f.write("</html>")
                create_index_page()
