const xhr = new XMLHttpRequest();
let currentUser;
let themes;

const USER_PAGE = "/user-page.html";

function closeThemeMenu() {
    const modalContainerThemes = document.getElementsByClassName("modal__container_themes")[0];
    modalContainerThemes.style.display = "none";
    removeChildren("frame__loaded_theme");
}

function closeCommonMenu() {
    const modalContainerCommon = document.getElementsByClassName("modal__container_common")[0];
    modalContainerCommon.style.display = "none";
}

function openCommonMenu() {
    const modalContainerCommon = document.getElementsByClassName("modal__container_common")[0];
    modalContainerCommon.style.display = "flex";
}

function closeOptionMenu() {
    const modalContainerOptions = document.getElementsByClassName("modal__container_options")[0];
    modalContainerOptions.style.display = "none";
    removeChildren("loaded_option");
}

function openOptionMenu() {
    const modalContainerOptions = document.getElementsByClassName("modal__container_options")[0];
    modalContainerOptions.style.display = "flex";

}


function showOptionsByTheme(event) {
    const selectTheme = event.currentTarget.previousElementSibling;
    const themeName = selectTheme.textContent;
    for (let theme of themes) {
        if (themeName === theme.themeName) {
            const options = theme.options;
            createLoadedOptionElements(options);
            break;
        }
    }
    openOptionMenu();
}

function createLoadedOptionElements(options) {
    const parentContainer = document.getElementsByClassName("modal__container_options_list")[0];
    options.forEach((option) => {
        const optionDivEl = document.createElement("div");
        optionDivEl.className = "loaded_option";
        optionDivEl.textContent = option.optionName;
        parentContainer.append(optionDivEl);
    })

}

function attachElToLookTheme() {
    const lookThemes = document.getElementsByClassName("look_theme");
    for (let lookTheme of lookThemes) {
        lookTheme.addEventListener("click", showOptionsByTheme)
    }
}

function removeChildren(childClassToRemove) {
    const childELs = document.getElementsByClassName(childClassToRemove);
    while (childELs.length > 0) {
        childELs[0].remove();
    }
}

function getThemes() {
    xhr.open("GET", "/themes")
    xhr.onload = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            themes = JSON.parse(xhr.responseText);
            openThemeMenu();
            embedThemes(themes);
            attachElToLookTheme();
            attachElToSelectTheme();
        }
    }
    xhr.send();
}

function generateQr() {
    const sessionId = currentUser.session.id;
    document.getElementById("sessionIdStr").value = sessionId;
    const directUrl = window.location.origin + USER_PAGE + "?sessionId=" + sessionId;
    new QRious({
        element: document.querySelector("#qr"),
        size: 200,
        value: directUrl
    });
    document.getElementsByClassName("modal__container_qr_code")[0].style.display = "flex";
}

function openThemeMenu() {
    const modalContainerThemes = document.getElementsByClassName("modal__container_themes")[0];
    modalContainerThemes.style.display = "flex";
}

function embedThemes(themes) {
    const parentElement = document.querySelector(".modal__container_themes_list");
    for (let theme of themes) {
        const divElementFrame = document.createElement("div");
        divElementFrame.className = "frame__loaded_theme";

        const divElementSelect = document.createElement("div");
        divElementSelect.className = "loaded_theme";
        divElementSelect.innerHTML = theme.themeName;

        const imageElement = document.createElement("img");
        imageElement.src = "image/magnifying-glass.png";
        imageElement.className = "look_image";

        const divElementLook = document.createElement("div");
        divElementLook.className = "look_theme";
        divElementLook.append(imageElement);


        divElementFrame.append(divElementSelect);
        divElementFrame.append(divElementLook);
        parentElement.append(divElementFrame);
    }
}

function initConnection(url, user) {
    xhr.open("POST", url)
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const savedUser = JSON.parse(xhr.responseText);
            currentUser = savedUser;
            connection();
        }
    }
    xhr.send(JSON.stringify(user));
    document.getElementsByClassName("modal__container_login")[0].style.display = "none"
}

function onOverflowElements(event) {
    const scrolledDiv = event.currentTarget;
    if (scrolledDiv.scrollHeight > scrolledDiv.clientHeight) {
        const marginOffset = document.getElementsByClassName("option_round")[0].offsetHeight / 2;
        if (scrolledDiv.scrollTop > marginOffset) {
            document.getElementById("top_overflow_indicator").style.display = "block";
        } else {
            document.getElementById("top_overflow_indicator").style.display = "none";
        }

        if (scrolledDiv.scrollTop + scrolledDiv.clientHeight + marginOffset > scrolledDiv.scrollHeight) {
            document.getElementById("bottom_overflow_indicator").style.display = "none";
        } else {
            document.getElementById("bottom_overflow_indicator").style.display = "block";
        }
    }
}

const mainModalBtn = document.getElementById("toggleContainerCommon");
const modalCommonMenuCloseBtn = document.getElementById("modalContainerCommonMenuCloseBtn");
const modalThemesCloseBtn = document.getElementById("modalContainerThemesCloseBtn");
const themeManagementDiv = document.querySelector(".management.theme");
const qrManagementDiv = document.querySelector(".management.qr");
const modalOptionsCloseBtn = document.getElementById("modalContainerOptionsCloseBtn");
const modalQrCodeCloseBtn = document.getElementById("modalContainerQrCodeCloseBtn");
const optionsContainerRound = document.getElementsByClassName("options__container_round")[0];

optionsContainerRound.addEventListener("scroll", onOverflowElements)
themeManagementDiv.addEventListener("click", getThemes);
qrManagementDiv.addEventListener("click", generateQr);
modalOptionsCloseBtn.addEventListener("click", closeOptionMenu);
modalThemesCloseBtn.addEventListener("click", closeThemeMenu);
modalCommonMenuCloseBtn.addEventListener("click", closeCommonMenu);
modalQrCodeCloseBtn.addEventListener("click", () => {
    document.getElementsByClassName("modal__container_qr_code")[0].style.display = "none";
})

mainModalBtn.addEventListener("click", openCommonMenu);