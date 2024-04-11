const {app, BrowserWindow} = require('electron') //634 (gzipped:392)

require("@electron/remote/main").initialize()

function createWindow() {
    const win = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true,
            enableRemoteModule: true
        }
    })

    win.loadURL('http://localhost:3000')
}

app.on("ready", createWindow)

