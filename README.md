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
Всего 3 экрана:
- **Список стримов**
- **Список пользователей в данной организации**
- **Мой профиль**

На главном экране приложения отображается список стримов: на которые мы подписаны, а также все существующие в данной организации.
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/channels_screen.png">
<br />
Пока актуальные стримы подгружаются с сервера, отображаются закешированные в бд стримы. В случае проблем с интернет-соединением, выводится ошибка.
У каждого стрима может быть неограниченное количество топиков:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/channels_screen_topic.png">
<br />
По клику на топик открывается чат этого топика:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/chat_screen.png">
<br />
Есть возможность отправить сообщение:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/sent_message_screen.png">
<br />
Добавить реакцию под сообщением (разработана **custom view** и **custom view group** для отображения блока с реакциями под сообщением). Либо по лонг тапу, тогда откроется bottom sheet с выбором эмодзи:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/choose_reaction.png">
<br />
Либо по клику на знак плюса (который появляется под сообщением, если есть как минимум 1 реакция от нас или от другого пользователя чата):
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/added_more_reaction.png">
<br />

Последние 50 сообщений каждого топика кешируются.

Также, на первом экране имеется поиск по **названию стрима**:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/search_stream.png">
<br />


На втором экране отображается списогк пользователей в текущей организации. В процессе загрузки отображается shimmer:
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/loading_users_screen.png">
<br />
<p>
<img src="https://raw.githubusercontent.com/7CreAtoR7/Messenger/readme/images/loaded_users_screen.png">
<br />


Используемый стек технологий:
- `Android SDK`
- `Kotlin`
- `Coroutines`
- `Retrofit2`
- `Okhttp3`
- `Room db`
- `Dagger2`
- `Elmslie`
- `Recycler delegates`