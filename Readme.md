# Epam Final Project - Autumn 2020

Требования к проекту
1. На основі сутностей предметної області створити класи, які їм відповідають.  
2. Класи і методи повинні мати назви, що відображають їх функціональність, і повинні бути  рознесені по пакетам.  
3. Оформлення коду має відповідати Java Code Convention.  
4. Інформацію щодо предметної області зберігати у реляційній базі даних (в якості СУБД  рекомендується використовувати MySQL або PostgreSQL). 
5. Для доступу до даних використовувати JDBC API із застосуванням готового або ж  розробленого самостійно пулу з'єднань. 
6. Застосунок має підтримувати роботу з кирилицею (бути багатомовним), в тому числі при  зберіганні інформації в базі даних:  


- повинна бути можливість перемикання мови інтерфейсу;  
- повинна бути підтримка введення, виведення і зберігання інформації (в базі даних),  записаної на різних мовах;  
- в якості мов обрати мінімум дві: одна на основі кирилиці (українська або російська),  інша на основі латиниці (англійська).  


7. Архітектура застосунка повинна відповідати шаблону MVC. 
8. При реалізації бізнес-логіки необхідно використовувати шаблони проектування: Команда,  Стратегія, Фабрика, Будівельник, Сінглтон, Фронт-контролер, Спостерігач, Адаптер та ін. 
9. Використовуючи сервлети і JSP, реалізувати функціональність, наведену в постановці  завдання.  
10. Використовувати Apache Tomcat у якості контейнера сервлетів.  
11. На сторінках JSP застосовувати теги з бібліотеки JSTL та розроблені власні теги (мінімум: один  тег custom tag library і один тег tag file). 
12. Реалізувати захист від повторної відправки даних на сервер при оновленні сторінки (реалізувати PRG). 
13. При розробці використовувати сесії, фільтри, слухачі.
14. У застосунку повинні бути реалізовані аутентифікація і авторизація, розмежування прав  доступу користувачів системи до компонентів програми. Шифрування паролів заохочується. 
15. Впровадити у проект журнал подій із використанням бібліотеки log4j.  
16. Код повинен містити коментарі документації (всі класи верхнього рівня, нетривіальні методи  і конструктори). 
17. Застосунок має бути покритим модульними тестами (мінімальний відсоток покриття 40%).  Написання інтеграційних тестів заохочуються. 
18. Реалізувати механізм пагінації сторінок з даними. 
19. Всі поля введення повинні бути із валідацією даних. 
20. Застосунок має коректно реагувати на помилки та виключні ситуації різного роду (кінцевий  користувач не повинен бачити stack trace на стороні клієнта). 
21. Самостійне розширення постановки задачі по функціональності заохочується! (додавання  капчі, формування звітів у різних форматах, тощо) 
22. Використання HTML, CSS, JS фреймворків для інтерфейсу користувача (Bootstrap, Materialize,  ін.) заохочується! 

# Тематика - Факультатив
1. Існує перелік курсів, розбитих за темами. За кожним курсом закріплений один викладач. Необхідно реалізувати наступну функціональність:

- сортування курсів за назвою (az, za), тривалістю, кількістю студентів, що записалися на курс;
- вибірка курсів, що належать до певної теми;
- вибірка курсів певного викладача.

2. Студент записується на один або декілька курсів, дані про реєстрацію зберігаються. По закінченню курсу викладач виставляє студенту оцінку, яка зберігається в журналі.
3. Кожен користувач має особистий кабінет, в якому відображена коротка інформація про користувача, а також для студента:
- перелік курсів, на які студент зареєструвався, але які ще не почалися;
- перелік курсів, на які студент зареєструвався і які знаходяться в прогресі;

- перелік пройдених курсів з інформацією про успішність;
для викладача:
- перегляд і редагування електронного журналу для закріплених за ним курсів.
Адміністратор системи володіє правами:
- реєстрації викладача і закріплення за ним курсу;
- додавання, видалення, редагування курсу;
- блокування, розблокування студента.


  - Import a HTML file and watch it magically convert to Markdown
  - Drag and drop images (requires your Dropbox account be linked)


You can also:
  - Import and save files from GitHub, Dropbox, Google Drive and One Drive
  - Drag and drop markdown and HTML files into Dillinger
  - Export documents as Markdown, HTML and PDF

Markdown is a lightweight markup language based on the formatting conventions that people naturally use in email.  As [John Gruber] writes on the [Markdown site][df1]

> The overriding design goal for Markdown's
> formatting syntax is to make it as readable
> as possible. The idea is that a
> Markdown-formatted document should be
> publishable as-is, as plain text, without
> looking like it's been marked up with tags
> or formatting instructions.

This text you see here is *actually* written in Markdown! To get a feel for Markdown's syntax, type some text into the left window and watch the results in the right.

### Tech

Dillinger uses a number of open source projects to work properly:

* [AngularJS] - HTML enhanced for web apps!
* [Ace Editor] - awesome web-based text editor
* [markdown-it] - Markdown parser done right. Fast and easy to extend.
* [Twitter Bootstrap] - great UI boilerplate for modern web apps
* [node.js] - evented I/O for the backend
* [Express] - fast node.js network app framework [@tjholowaychuk]
* [Gulp] - the streaming build system
* [Breakdance](https://breakdance.github.io/breakdance/) - HTML to Markdown converter
* [jQuery] - duh

And of course Dillinger itself is open source with a [public repository][dill]
 on GitHub.

### Installation

Dillinger requires [Node.js](https://nodejs.org/) v4+ to run.

Install the dependencies and devDependencies and start the server.

```sh
$ cd dillinger
$ npm install -d
$ node app
```

For production environments...

```sh
$ npm install --production
$ NODE_ENV=production node app
```

### Plugins

Dillinger is currently extended with the following plugins. Instructions on how to use them in your own application are linked below.

| Plugin | README |
| ------ | ------ |
| Dropbox | [plugins/dropbox/README.md][PlDb] |
| GitHub | [plugins/github/README.md][PlGh] |
| Google Drive | [plugins/googledrive/README.md][PlGd] |
| OneDrive | [plugins/onedrive/README.md][PlOd] |
| Medium | [plugins/medium/README.md][PlMe] |
| Google Analytics | [plugins/googleanalytics/README.md][PlGa] |


### Development

Want to contribute? Great!

Dillinger uses Gulp + Webpack for fast developing.
Make a change in your file and instantaneously see your updates!

Open your favorite Terminal and run these commands.

First Tab:
```sh
$ node app
```

Second Tab:
```sh
$ gulp watch
```

(optional) Third:
```sh
$ karma test
```
#### Building for source
For production release:
```sh
$ gulp build --prod
```
Generating pre-built zip archives for distribution:
```sh
$ gulp build dist --prod
```
### Docker
Dillinger is very easy to install and deploy in a Docker container.

By default, the Docker will expose port 8080, so change this within the Dockerfile if necessary. When ready, simply use the Dockerfile to build the image.

```sh
cd dillinger
docker build -t joemccann/dillinger:${package.json.version} .
```
This will create the dillinger image and pull in the necessary dependencies. Be sure to swap out `${package.json.version}` with the actual version of Dillinger.

Once done, run the Docker image and map the port to whatever you wish on your host. In this example, we simply map port 8000 of the host to port 8080 of the Docker (or whatever port was exposed in the Dockerfile):

```sh
docker run -d -p 8000:8080 --restart="always" <youruser>/dillinger:${package.json.version}
```

Verify the deployment by navigating to your server address in your preferred browser.

```sh
127.0.0.1:8000
```

#### Kubernetes + Google Cloud

See [KUBERNETES.md](https://github.com/joemccann/dillinger/blob/master/KUBERNETES.md)


### Todos

 - Write MORE Tests
 - Add Night Mode

License
----

MIT


**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
