const sessionId = window.location.search.replace("?sessionId=", "");

function connectToSession() {
    const username = document.getElementById("inputLogin").value.trim();
    if (username) {
        const userDTO = {
            userName: username,
            role: "USER",
            isActive: false,
            session: {
                id: sessionId
            }
        };
        initConnection("/user", userDTO);
    }
}

const userConnectToSession = document.getElementById("userConnectToSessionBtn");
userConnectToSession.addEventListener("click", connectToSession);
