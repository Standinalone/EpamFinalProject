# Epam Final Project - Autumn 2020

*Основные технологии: Tomcat 9.0, MYSQL, JSTL*

## Требования к проекту

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

## Тематика - Факультатив
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

### Схема базы данных

![alt text](https://i.ibb.co/jH4qSdb/Model.png "Модель")

### Запуск

1. Скачать репозиторий
2. Установить Tomcat 9.0
3. Настроить соединение к базе данных (*/EpamFinalProject/src/main/webapp/META-INF/context.xml*)
4. Настроить провайдера SMTP (*/EpamFinalProject/src/main/resources/app.properties*)
5. Создать базу данных "epam" используя MYSQL server
6. Запустить последовательно sql скрипты


  - /EpamFinalProject/sql/createTables.sql
  - /EpamFinalProject/sql/fillUsers.sql


А также при необходимости:

7. Изменить настройки проекта (*/EpamFinalProject/src/main/java/com/epam/project/constants/Constants.java*)
8. Изменить настройки логгирования (*/EpamFinalProject/src/main/resources/log4j.properties*)

*После запуска перейти по адресу http://localhost:8080/EpamFinalProject/controller?command=HOME_PAGE*

### Todos

 - Обеспечить более полное покрытие тестами
 - Добавить реализацию DAO для других СУБД
 - Возможность изменять пароль
 - Создание различных отчетов
 - Защита от DDoS атак
 - Добавить полномочия, рефакторинг кода и т.д.