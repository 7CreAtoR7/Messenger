# Messenger
Это Android-приложение аналог Discord, которое я разрабатывал в процессе обучения в Тинькофф Финтех по направлению Android-разработчик под руководством ментора.

Приложение написано по принципу **Clean Architecture** с применением паттерна презентационного слоя **MVI** с библиотекой **elmslie** от компании vivid-money.

vivid-money - компания от бывших топ-менеджеров Тинькофф.

[Elmslie](https://github.com/vivid-money/elmslie) - опенсурсная библиотека для удобной работы с MVI.

Представление архитектуры:
<p>
<img src="https://user-images.githubusercontent.com/16104123/115949827-40b27980-a4e0-11eb-85dc-03a7073e3127.png" width="500">
<br />


Это приложение-мессенджер, где в роли backend выступает сервис [Zulip api](https://zulip.com/api/)


## Реализация приложения

Используемый стек технологий:
- `Android SDK`
- `Kotlin`
- `Coroutines + Flow`
- `Retrofit2`
- `Okhttp3`
- `Room db`
- `Dagger2`
- `Elmslie`
- `Recycler delegates`


При запуске приложения есть 3 экрана:
- **Список стримов**
- **Список пользователей в данной организации**
- **Мой профиль**

На главном экране приложения отображается список стримов: на которые мы подписаны, а также все существующие в данной организации.
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/channels_screen.jpg" width="400" height="800">
<br />


<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/all_streams_screen.jpg" width="400" height="800">
<br />



Пока актуальные стримы подгружаются с сервера, отображаются закешированные в бд стримы. В случае проблем с интернет-соединением отображается snackbar с ошибкой.

У каждого стрима может быть неограниченное количество топиков:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/channels_screen_topic.jpg" width="400" height="800">
<br />


По клику на топик открывается чат этого топика, где могут отображаться сообщения других пользователей:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/chat_screen.jpg" width="400" height="800">
<br />


Есть возможность отправить сообщение:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/sent_message_screen.jpg" width="400" height="800">
<br />


Также можно добавить реакцию на сообщение (разработана **custom view** и **custom view group** для отображения блока с реакциями под сообщением).
Добавление осуществляется либо по лонг тапу на сообщение (тогда откроется bottom sheet с выбором эмодзи):
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/choose_reaction.jpg" width="400" height="800">
<br />


Либо по клику на знак плюса (который появляется под сообщением, если есть как минимум 1 реакция от нас или от другого пользователя чата):
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/added_more_reaction.jpg" width="400" height="800">
<br />

Последние 50 сообщений каждого топика кешируются.


Также, на первом экране имеется поиск по **названию стрима**:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/search_stream.jpg" width="400" height="800">
<br />



На втором экране отображается список пользователей в текущей организации. В процессе загрузки отображается shimmer:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/loading_users_screen.jpg" width="400" height="800">
<br />


<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/master/images/loaded_users_screen.jpg" width="400" height="800">
<br />
