<?php

include_once "api.php";

$users = json_decode(file_get_contents("private/users.json"));

api("mainframe", function ($action, $parameters) {
    global $users;
    if ($action === "checkname") {
        if (isset($parameters->name)) {
            foreach ($users as $user) {
                if ($user[0] === $parameters->name)
                    return [true, $user];
            }
            return [false, null];
        }
        return [false, "Missing arguments"];
    } else if ($action === "checkpassword") {
        if (isset($parameters->name) && isset($parameters->password)) {
            foreach ($users as $user) {
                if ($user[0] === $parameters->name)
                    if ($user[1] === duth_hash($parameters->password, $user[2])) {
                        return [true, "<p>Authentication OK</p><!--For mainframe authentication use 'nc ctf.kaf.sh 1111'-->"];
                    } else {
                        return [false, null];
                    }
            }
            return [false, null];
        }
        return [false, "Missing arguments"];
    }
    return [false, "Unknown action"];
});

echo json_encode($result);

function duth_hash($secret, $salt, $rounds = 10)
{
    if ($rounds === 0)
        return sha1($secret . $salt);
    return sha1($salt . duth_hash($secret, $salt, $rounds - 1));
}