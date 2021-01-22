from keywords import Keywords 

from porter import Stemmer
from nltk.tokenize import sent_tokenize, word_tokenize

import webbrowser
import os
import sys
import subprocess 
import re

class Console:

    def ProccessCommand(self,command):
        if not command:
            print("I can't guess.")
            return

    #1 
    #(wstępny proces wywołania funkcji stem na wpisanej komendzie )
    def PreProccessCommand(self,command):

        command = command.lower()
        command = command.replace('[^a-z\\s]','')
        commandArray = command.split('\\s') 
        stemmer = Stemmer()
        resultWords = stemmer.stemSentence(command)

        return resultWords
        


    #sprawdzenie czy, któreś ze słów znajduje sie w KeyWords we funkcji witającej (def Greetings(self)) 
    def checkGreetings(self,preproccessCommand):

        keyword = Keywords()
        keyWords = keyword.Greetings()
        for k in keyWords:
            if k in preproccessCommand:
                return True
        return False


    def run_checkProgram(self, preproccessCommand):

        keyword = Keywords()
        keyWords = keyword.RunProgram()
        for k in keyWords:
            if k in preproccessCommand :      
                return True
        return False



    def close_checkProgram(self,preproccessCommand):

        keyword = Keywords()
        keyWords = keyword.CloseProgram()
        for k in keyWords:
            if k in preproccessCommand:
                return True
        return False


    def checkDocument(self,preproccessCommand):

        keyword = Keywords()
        keyWords = keyword.OpenDocument()
        for k in keyWords:
            if k in preproccessCommand:
                return True
        return False

    
    def checkBrowser(self,preproccessCommand):

        keyword = Keywords()
        keyWords = keyword.RunBrowser()
        for k in keyWords:
            if k in preproccessCommand:
                
                return True
        return False



def openprogram(file):
    #file = '"C:\\Users\\Ola\\AppData\\Local\\Discord\\app-0.0.309\\" + file + ".exe"'
    os.system("C:\\Users\\Ola\\AppData\\Local\\Discord\\app-0.0.309\\" + file + ".exe")
    #subprocess.call([file])
    print("Starting program...")

def closeprogram():
    os.system("cd C:\\Users\\Ola\\AppData\\Local\\Discord\\app-0.0.309\\")
    os.system("taskkill /im Discord.exe")
    print("Shutting down application...")

def opendocument():
    file = input("Name of a document: ")
    os.system(file + ".txt")
    print("Opening document...")

def openwebbrowser(link):
    webbrowser.open(link + ".com")
    print("Searching for in browser...")


print("Hello!")


while 1:

    if __name__ == '__main__':
        
        

        obiekt = Console()
        command = input()
        if obiekt.ProccessCommand(command):
            

            command = input()
            m = obiekt.PreProccessCommand(command)

        else :
            m = obiekt.PreProccessCommand(command)
            if obiekt.checkGreetings(m):
                print("Hi! Tell me what you need?")
                command = input()
                m = obiekt.PreProccessCommand(command)

            if obiekt.run_checkProgram(m):
                file  = input("Enter program you want to run: ")
                openprogram(file)

                print("Now what to do?: ")
                command = input()
                m = obiekt.PreProccessCommand(command)
                if obiekt.close_checkProgram(m):
                    closeprogram()

            elif obiekt.checkDocument(m):
                opendocument()
                print("Is there something else I can do for you?: ")

            elif obiekt.checkBrowser(m):
                link = input("Enter side you want to open:")
                openwebbrowser(link)
        



        




 


   