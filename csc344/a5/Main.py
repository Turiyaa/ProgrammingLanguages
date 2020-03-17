import  os
import tarfile
if __name__ == '__main__':
    os.system('python ZipToShip.py')
    os.system('tar -czvf csc344.tar.gz csc344')
    email = input("Enter email: ")
    os.system('mutt -s "Assignment-5" ' +email+ ' -a /home/nneopane/assignment-5.tar.gz <body.txt')