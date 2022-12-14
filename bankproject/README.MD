### Задание.
<br>
Необходимо создать REST-сервис для проведения финансовых операций клиентов.
<br>Структура данных:
<br>Клиент
<br>Идентификатор (порядковый номер)
<br>Фамилия
<br>Имя
<br>Отчество
<br>Секретное слово для выполнения операций(хранить в открытом виде запрещено)
<br>
-----------------------------------------
<br> Счет клиента
<br>Идентификатор
<br>Идентификатор клиента
<br>Номер счета
<br>Сумма
<br>Тип счета
<br>Дата открытия
<br>Срок действия
<br>Кассовый ордер<br>
-----------------------------------------
<br>Идентификатор
<br>Тип ордера (пополнение, снятие)
<br>Сумма
<br>Идентификатор счета клиента
<br>Результат выполнения
<br>Дата создания
<br>Транзакция<br>
-----------------------------------------
<br>Идентификатор
<br>Дата создания
<br>Сумма
<br>Тип (пополнение, снятие, перевод)
<br>Идентификатор счета клиента
<br>Идентификатор кассового ордера (заполняется в случае Типа пополнение, снятие)
<br>Идентификатор счета клиента с которого выполняется перевод (заполняется в случае Типа перевод)
<br>Результат выполнения
<br>REST-сервис должен предоставлять следующие методы:
<br>- Получить информацию обо всех клиентах
<br>- Получить информацию о клиенте по его идентификатору
<br>- Получить информацию о счетах клиента
<br>- Получить информацию о транзакциях по счету клиента
<br>- Получить информацию о кассовых ордерах по счету клиента
<br>- Создать кассовый ордер
<br>  -- в зависимости от типа кассового ордера должна измениться сумма счета и создаться транзакция
  <br>  -- при создании кассового ордера на снятие должна выполнятся проверка секретного слова, в случае
  если оно не совпадает, должна возвращаться ошибка, но записи в таблицах Кассовый ордер и
  Транзакция должны быть созданы и отображать причину почему операция не выполнена
- Создать перевод между счетами одного пользователя
  <br>  -- при создании транзакции должна выполнятся проверка секретного слова, в случае если оно не
  совпадает, должна возвращаться ошибка, но записи в таблице Транзакция должны быть созданы и
  отображать причину почему операция не выполнена
- Создать перевод между счетами разных пользователей
  <br>  -- при создании транзакции должна выполнятся проверка секретного слова, в случае если оно не
  совпадает, должна возвращаться ошибка, но записи в таблице Транзакция должны быть созданы и
  отображать причину почему операция не выполнена
 <br> Сервис при первом запуске должен самостоятельно создавать необходимые объекты в БД с помощью
  Liquibase.
 <br> Необходимо использовать PostgreSQL.
  Для реализации необходимо использовать Java 8, Spring Boot, Hibernate, PostgreSQL, Liquibase.
  Исходный код загрузить в GitHub и в описании указать краткую инструкцию по запуску приложения.



### Решение.

В базе данных изначально есть 4 клиента и у каждого несколько счетов. Все операции быстро и просто заполняются с помощью команд: "Перевод между счетами, создание кассового ордера"

Есть 4 клиента
1. Petrov Maksim Ivanovich (password: MPetrIva389) - имеет два дебетовых счета
2. Dorn Ivan Sergeevich (password: DornVan777) - имеет два дебетовых и один кредитный счет
3. Kirkorov Egor Stepanovich (password: 532EgoKir) - имеет два дебетовых счета
4. Vershinina Irina Osipovna (password: IraOV333) - имеет один дебетовый счет

Отношение сущностей можно посмотреть на Dia.jpg

### Краткая инструкция:

- Получить информацию о всех клиентах. GET запрос на "/client/all"
- Получить информацию о клиенте по id. GET запрос на "/client/{ввести номер id}" (Для примера можно ввести: 1,2,3,4)
- Получить информацию о счетах клиента. GET запрос на "/client/accounts/{ввести номер id}" (Для примера можно ввести: 1,2,3,4)

- Перевод между счетами одного пользователя:
    Отправить Json объект по POST запросу на адрес "/transaction/self".
<br> С какого счета списать деньги "ключ: numberSend"
<br> На какой счет отправить деньги "ключ: numberCome"
<br> Сумма которую нужно перечислить: "ключ: amount"
<br> Секретное слово: "ключ: password"
<br> Пример: {
  "numberSend": "987561",
  "numberCome": "987562",
  "amount": "10000",
  "password": "MPetrIva389"
  }
<br> Проходит ТРИ проверки: секретное слово, принадлежат ли счета одному клиенту, хватает ли средств для перевода.
<br> В случае удачного перевода, создается две транзакции. В случае неудачи, только одна транзакция для счета с которого пытались списать деньги.

- Перевод между счетами разных пользователей:
Отправить Json объект по POST запросу на адрес "/transaction/toClient".
<br> С какого счета списать деньги "ключ: numberSend"
  <br> На какой счет отправить деньги "ключ: numberCome"
  <br> Сумма которую нужно перечислить: "ключ: amount"
  <br> Секретное слово: "ключ: password" (СЕКРЕТНОЕ СЛОВА ДЛЯ СЧЕТА КЛИЕНТА С КОТОРОГО СПИСЫВАЮТ ДЕНЬГИ)
  <br> Пример: {
  "numberSend": "987577",
  "numberCome": "987561",
  "amount": "10000",
  "password": "532EgoKir"
  }
  <br> Проходит ДВЕ проверки: секретное слово и хватает ли средств для перевода.
  <br> В случае удачного перевода, создается две транзакции. В случае неудачи, только одна транзакция для счета с которого пытались списать деньги.
 - Создание кассового ордера.
   Отправить Json объект по POST запросу на адрес "/transaction/cashOrder".
   <br> Номер счета с котором производятся операции "ключ: number"
   <br> Тип операции "ключ: order" (Возможно два варианта: ADD, WITHDRAW)
   <br> Сумма которую нужно использовать в операции: "ключ: amount"
   <br> Секретное слово: "ключ: password" для пополнения можно не использовать секретное слово.
   <br> Пример: {
   "number": "999999",
   "order": "ADD",
   "amount": "50",
   "password": "IraOV333"
   }
<br> Пример второй {
   "number": "987777",
   "order": "WITHDRAW",
   "amount": "555",
   "password": "DornVan777"
   }
   <br> На снятие проходит ДВЕ проверки: секретное слово и хватает ли средств для перевода.
<br> При пополнении проверка на секретное слово не производиться("ADD").
 <br>

- Если POST запросы выше сделаны, то и команды ниже будут уже не пустыми.
- Получить информацию о транзакция по счету клиента. GET запрос на "/transaction/{ввести номер счета клиента}" (По умолчанию здесь пусто, но их можно быстро заполнить с помощью команд: "Перевод между счетами, создание кассового ордера")
<br> Пример /transaction/987561
- Получить информацию о кассовых ордерах по счету клиента. GET запрос на "/cash_order/{ввести номер счета клиента}" (По умолчанию здесь пусто, но их можно быстро заполнить с помощью команд: "Перевод между счетами, создание кассового ордера")
<br> Пример /cash_order/999999
  <br> Пример /cash_order/987777
