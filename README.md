# GitMock

## CSCI 2020U - Software Systems Development & Integration
Course Final Project <br>
Faculty of Science, Ontario Tech University <br>
April 14, 2022 <br>

## Project Group 13:
| Group Member Name | Student ID | GitHub Username (GitHub URL)|
| :------------------------:|:------------------------:|:--------------------------------------:|
| Caleb Gladwin | 100755915 | [CGladwin](https://github.com/CGladwin) |
| Preet Panchal | 100707094 | [preet-panchal](https://github.com/preet-panchal) |
| Eihab Syed | 100707448 | [EihabSyed](https://github.com/EihabSyed) |
| Nathaniel Tai | 100662284 | [NathanTai](https://github.com/NathanTai) |

## About GitMock:
GitMock is a simple mock application of GitHub Desktop. In CSCI2020U, we
learned about File IO, Multithreading and Sockets. Using JavaFX and Gradle,
our group decided to take inspiration from the GitHub desktop app to create
a simple gradle app which supports Client/Server architecture. GitMock allows
all users to connect to a specific server and use GitHub commands such as:
Git `push`, `pull` and `fetch` to move and update files from the Local repo
and Remote repo.

## How to Run:
**Application Build:** Java version 17.0.2, Gradle version 7.3.3

1) Clone repo or download ZIP folder
2) Open project via IntelliJ IDEA (https://www.jetbrains.com/idea/)
3) Within project structure (on the left hand side) and expand src/main/java:
    1) Right-click on FileServer and select `Run 'FileServer.main()`
    2) Right-click on Main and select `Run 'Main.main()'`
4) Now the application should be running on `port 8081`. You may now make changes
   to your local repo files inside the `ClientFiles` folder.

## Demo:
Please watch this short demo video to get help running the app or
simply view the app: https://www.youtube.com/watch?v=H-XyoPv3EuY