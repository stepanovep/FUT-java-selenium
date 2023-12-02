## Installation guide. For myself :)

- Run docker-compose to start mongodb local server `docker-compose up -d`
- Set env vars for telegram_bot_token
- In application.yaml
  - Set path to chromedriver executable
  - Choose platform: PC or CONSOLE

### Troubleshooting
- Be sure docker daemon is running: `colima start` or start via Docker Desktop UI
- Error on startup: `org.openqa.selenium.SessionNotCreatedException: Could not start a new session`:
  Open chromedriver executable via terminal

## Features

 - Autobidding 83-87 rated gold players
 - Automated BPM (Bronze Pack Method)
 - Run commands via telegram bot
 - No new features coming, migrating bot to Chrome extension 

<br/>
Telegram bot screenshot examples
<br/>
<br/>
<img alt="Alt text" src="src/main/resources/screenshots/telegram-bot-demo.jpg?raw=true" title="Demo" width="300"/>
