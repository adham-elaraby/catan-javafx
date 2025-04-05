# FOP-Projekt (Archive)

Implementation of the popular board game "Catan" as part of a Voulountary Programming Project for the TU Darmstadt Informatik course. This project showcases advanced Java programming techniques, Design Patterns, and utilizes JavaFX for the user interface.

## Table of Contents
- [Screenshots](#screenshots)
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)

## Screenshots

![Menu](https://github.com/user-attachments/assets/4016f43a-7133-46bb-9502-5fc3b2e486b4)           |  ![Build-Road](https://github.com/user-attachments/assets/fab90630-f017-4e6d-be19-eb4ad0908a6f)
:-------------------------:|:-------------------------:
![Start-Game](https://github.com/user-attachments/assets/3e2d9e8d-c9ba-42c7-979a-acf3a1c955d9)  |  ![Trade](https://github.com/user-attachments/assets/2c9ec8dc-367d-4189-9c8f-18344c149ec3)

## Introduction
This repository contains the digital implementation of the popular board game "Catan". The project is designed to provide a rich graphical and interactive experience, allowing users to play the game as they would in a physical setting.

## Features
- Full implementation of the Catan game rules.
- Interactive user interface designed with JavaFX.
- Multiplayer capabilities.
- Save and load game functionality.

## Technologies Used
- **Java**: The primary programming language used for the game logic.
- **JavaFX**: Used for creating the graphical user interface.
- **Gradle**: For build automation.

### Concepts
- Lambdas
- Streams
- Optionals
- Generics

## Setup Instructions
To set up and run the project, follow these steps:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/adham-elaraby/FOP-2324-Projekt-Student.git
   cd FOP-2324-Projekt-Student
   ```

2. **Install Dependencies**:
   Ensure you have Java and Gradle installed on your machine. Then, run the following command to download and install the required dependencies:
   ```bash
   ./gradlew build
   ```

3. **Run the Application**:
   To start the application, use the following command:
   ```bash
   ./gradlew run
   ```

## Usage
- **Pull, Edit, Commit, Push**: Follow these steps to make and push changes:
  1. `git pull origin main`
  2. Edit files
  3. `git add <filename>`
  4. `git commit -m "Description of changes"`
  5. `git push origin main`

- **Increase Memory**: If you encounter memory issues, increase the memory allocation by editing the `gradle.properties` file:
  ```properties
  org.gradle.jvmargs=-Xmx8192M
  ```

For more detailed commands and instructions, refer to the [Help Section](#help) in the repository.


---





## Help
- [Studierenden-Guide](https://wiki.tudalgo.org/)
- [Link to Public Repo (Upstream)](https://github.com/FOP-2324/FOP-2324-Projekt-Student)
- [Link to Moodle Page](https://moodle.informatik.tu-darmstadt.de/course/view.php?id=1469&sectionid=18783)

## Important Commands

### Quick Walkthrough

Pull -> Edit files -> git add -> commit -> push

1. Always pull before working, and before pushing
`git pull origin main`

2. After implementing a task or a couple of tasks e.g. H2.1 + H2.2 do the following:
`git status` to see the status of any untracked changes

3. Add all the files that you changed that need to be tracked
`git add example-file.java`

4. Commit with message
`git commit -m "Enter a discription of what task you did"`

5. Push.
`git push origin main`

### Commands

To Pull each others private changes
`git pull origin main`

To Push your commits to private
`git push origin main`

(Only for Adham) To Pull any changes from public
`git pull public main`

## Increase Memory to be able to Run the gradle task `verification\check`

### End the task, i.e. make intellij stop running then:
run: `./gradlew --stop` in intellij terminal.

### Java mehr Speicher zuweisen
Gehen Sie in Ihrem User Ordner in der Ordner ".gradle"
Unter Windows finden Sie den Ordner unter `"C:\Users\<UserName>\.gradle"` und auf Linux und macOS unter `"~/.gradle"`
Falls nicht vorhanden, erstellen Sie die Datei `"gradle.properties"`.
Fügen Sie in dieser Datei eine Zeile mit folgendem Eintrag hinzu:

`org.gradle.jvmargs=-Xmx8192M`
Sie können auch versuchen die Zahl am Ende zu erhöhen, wenn der Fehler weiterhin auftritt.

## Setup! DO NOT SKIP
### Install GitHub Client

### MacOS
1. `brew install gh` if you have Homebrew installed, otherwise download from the GitHub website
2. Login to GitHub with Terminal
3. run `gh auth login` and choose website login

Then clone the repo, and run the following commands in the directory:

#### Optional, you don't need to run the next command (Adham will handle this)
- Add the public repo as public
`git remote add public https://github.com/FOP-2324/FOP-2324-Projekt-Student.git`

### Old instructions don't follow:
- Make git not change `build.gradle.kts` because we all have different tu-id's
`git update-index --assume-unchanged build.gradle.kts`

