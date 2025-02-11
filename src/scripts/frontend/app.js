let user = null;

function load() {
    page("home");
}

function userinput(inputElement) {
    inputElement.value = inputElement.value.toLowerCase();
    api("files/dumbuth/dumbuth.php", "mainframe", "checkname", {name: inputElement.value}, (success, result, error) => {
        if (success) {
            user = result;
            get("username-next").removeAttribute("disabled");
            get("username-valid").innerText = "V";
        } else {
            get("username-valid").innerText = "X";
            get("username-next").setAttribute("disabled", "");
        }
    });
}

function passwordinput(inputElement) {
    if (user !== null) {
        inputElement.value = inputElement.value.toLowerCase();
        inputElement.value = inputElement.value.replace(/[^a-z]/g,"");
        if (user[1] === duth_hash(inputElement.value, user[2])) {
            page("authenticated");
            api("files/dumbuth/dumbuth.php", "mainframe", "checkpassword", {name: user[0], password: inputElement.value}, (success, result, error) => {
                if (success) {
                    get("authenticated").innerHTML = result;
                } else {
                    get("authenticated").innerText = "Auth failed, " + error;
                }
            });
        }
    }
}

function duth_hash(secret, salt, rounds = 10) {
    if (rounds === 0)
        return sha1(secret + salt);
    return sha1(salt + duth_hash(secret, salt, rounds - 1));
}

function sha1(str) {
    //  discuss at: http://locutus.io/php/sha1/
    // original by: Webtoolkit.info (http://www.webtoolkit.info/)
    // improved by: Michael White (http://getsprink.com)
    // improved by: Kevin van Zonneveld (http://kvz.io)
    //    input by: Brett Zamir (http://brett-zamir.me)
    //      note 1: Keep in mind that in accordance with PHP, the whole string is buffered and then
    //      note 1: hashed. If available, we'd recommend using Node's native crypto modules directly
    //      note 1: in a steaming fashion for faster and more efficient hashing
    //   example 1: sha1('Kevin van Zonneveld')
    //   returns 1: '54916d2e62f65b3afa6e192e6a601cdbe5cb5897'

    var hash
    try {
        var crypto = require('crypto')
        var sha1sum = crypto.createHash('sha1')
        sha1sum.update(str)
        hash = sha1sum.digest('hex')
    } catch (e) {
        hash = undefined
    }

    if (hash !== undefined) {
        return hash
    }

    var _rotLeft = function (n, s) {
        var t4 = (n << s) | (n >>> (32 - s))
        return t4
    }

    var _cvtHex = function (val) {
        var str = ''
        var i
        var v

        for (i = 7; i >= 0; i--) {
            v = (val >>> (i * 4)) & 0x0f
            str += v.toString(16)
        }
        return str
    }

    var blockstart
    var i, j
    var W = new Array(80)
    var H0 = 0x67452301
    var H1 = 0xEFCDAB89
    var H2 = 0x98BADCFE
    var H3 = 0x10325476
    var H4 = 0xC3D2E1F0
    var A, B, C, D, E
    var temp

    // utf8_encode
    str = unescape(encodeURIComponent(str))
    var strLen = str.length

    var wordArray = []
    for (i = 0; i < strLen - 3; i += 4) {
        j = str.charCodeAt(i) << 24 |
            str.charCodeAt(i + 1) << 16 |
            str.charCodeAt(i + 2) << 8 |
            str.charCodeAt(i + 3)
        wordArray.push(j)
    }

    switch (strLen % 4) {
        case 0:
            i = 0x080000000
            break
        case 1:
            i = str.charCodeAt(strLen - 1) << 24 | 0x0800000
            break
        case 2:
            i = str.charCodeAt(strLen - 2) << 24 | str.charCodeAt(strLen - 1) << 16 | 0x08000
            break
        case 3:
            i = str.charCodeAt(strLen - 3) << 24 |
                str.charCodeAt(strLen - 2) << 16 |
                str.charCodeAt(strLen - 1) <<
                8 | 0x80
            break
    }

    wordArray.push(i)

    while ((wordArray.length % 16) !== 14) {
        wordArray.push(0)
    }

    wordArray.push(strLen >>> 29)
    wordArray.push((strLen << 3) & 0x0ffffffff)

    for (blockstart = 0; blockstart < wordArray.length; blockstart += 16) {
        for (i = 0; i < 16; i++) {
            W[i] = wordArray[blockstart + i]
        }
        for (i = 16; i <= 79; i++) {
            W[i] = _rotLeft(W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16], 1)
        }

        A = H0
        B = H1
        C = H2
        D = H3
        E = H4

        for (i = 0; i <= 19; i++) {
            temp = (_rotLeft(A, 5) + ((B & C) | (~B & D)) + E + W[i] + 0x5A827999) & 0x0ffffffff
            E = D
            D = C
            C = _rotLeft(B, 30)
            B = A
            A = temp
        }

        for (i = 20; i <= 39; i++) {
            temp = (_rotLeft(A, 5) + (B ^ C ^ D) + E + W[i] + 0x6ED9EBA1) & 0x0ffffffff
            E = D
            D = C
            C = _rotLeft(B, 30)
            B = A
            A = temp
        }

        for (i = 40; i <= 59; i++) {
            temp = (_rotLeft(A, 5) + ((B & C) | (B & D) | (C & D)) + E + W[i] + 0x8F1BBCDC) & 0x0ffffffff
            E = D
            D = C
            C = _rotLeft(B, 30)
            B = A
            A = temp
        }

        for (i = 60; i <= 79; i++) {
            temp = (_rotLeft(A, 5) + (B ^ C ^ D) + E + W[i] + 0xCA62C1D6) & 0x0ffffffff
            E = D
            D = C
            C = _rotLeft(B, 30)
            B = A
            A = temp
        }

        H0 = (H0 + A) & 0x0ffffffff
        H1 = (H1 + B) & 0x0ffffffff
        H2 = (H2 + C) & 0x0ffffffff
        H3 = (H3 + D) & 0x0ffffffff
        H4 = (H4 + E) & 0x0ffffffff
    }

    temp = _cvtHex(H0) + _cvtHex(H1) + _cvtHex(H2) + _cvtHex(H3) + _cvtHex(H4)
    return temp.toLowerCase()
}