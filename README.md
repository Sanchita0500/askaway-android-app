# AskAway

## Introduction

AskAway is like a doubt forum where students can ask their doubts to professors personally or to everyone in general. Doubt solving is a major issue during online semesters as we ask each other anything through whatsapp only so if a question is asked and answered, referring it next time requires a lot of searching and scrolling.

My app is aimed at solving this issue particularly. Sometimes students feel shy in asking questions so my app also has a feature of asking questions anonymously to a professor or to everyone in general. All the answered questions are visible on home screen so that everyone can benefit and the professor's don't have to answer the same questions for everyone individually.

It is an android application and I have used Firebase for database connection.

## Users
- Admin : Only admin can add or remove faculties
- Students : Students can signup using their unique student id provided by college
- Faculties : Faculty accounts are created by admin and they can login using their faculty id provided by college.

## Feaures

- On launching app, it first shows login page. If the student is not registered they can click on signup link. 
- To open admin page the askaway logo is converted to an image button, on clicking the logo admin login page opens. This is a backdoor for admin to use the app, general user won't know about this.

 <img src="https://i.imgur.com/DBrhcXQ.png" width="200"> <img src="https://i.imgur.com/QjqBGIB.png" width="200"> <img src="https://i.imgur.com/6X5HvS0.png" width="200">
 
 - Admin can add faculties. This is done so that when students ask questions they have a list of faculties to choose from. If a faculty registers directly to the app then he/she will be treated like a general user i.e. their names won't be visible in fcaulty list. To maintain genuinity of answers I thought of this admin side of the app.
 - An admin can also remove faculty i.e. delete their account.

<img src="https://i.imgur.com/wqmAPbA.png" width="200"> <img src="https://i.imgur.com/DTtZwFu.png" width="200"> <img src="https://i.imgur.com/B1BrEEx.png" width="200">

- When user logs in from login page, he/she will see a home screen filled with answered questions.
- I have implemented real time search feature here on homescreen so that anyone can search for a question and check if it has already been answered.
- The drawer layout has many go-to links and it also greets the user i.e. if Sanchita has logged in it will say "Hello, Sanchita!"
- If you click on "view more" option to read the full answer it will open another screen showing detailed answer. 

<img src="https://i.imgur.com/rRdjqOW.png" width="300"> <img src="https://i.imgur.com/ZtXNPgJ.png" width="300">

- "Ask a Question" page has a list of faculties from which student can choose whom to ask a question or there is an option to ask a question to everyone as well.
- The students can ask questions anonymously also by clicking on "Ask as anonymous checkbox"
- "Answer a Question" page is personalized for every user. If some question is asked to Everyone then that is visible on this page for every account till it is answered otherwise only professors have questions on this page if some student asks them something.

<img src="https://i.imgur.com/xeJ3FE3.png" width="300"> <img src="https://i.imgur.com/z4SKP6h.png" width="300">

- A user can see all the questions asked or answered by him/her in the Profile section. 
- Delete account feature is also implemented.

<img src="https://i.imgur.com/4XOUj4M.png" width="300"> <img src="https://i.imgur.com/iljL15Y.png" width="300">

## Android app download link

- My app is targeted at API 23 so it works on 98% android devices. Please click on below link to download it on your android device.
[https://drive.google.com/drive/folders/1bQClGbK-1kr7FD_9W43f_FgPq5PwGelj?usp=sharing]()


## Credentials

- Admin user id : admin
- Admin password : admin@125
- I am providing here some professor's login id and password so that "Answer a question" feature can be tested well



| Name | faculty id | password |
| -------- | -------- | -------- |
| Neelesh Kumar | 10203 | 10203 |
| Mansi Radke | 51312 | 51312 |
| UA Deshpande | 67890 | 67890 |







