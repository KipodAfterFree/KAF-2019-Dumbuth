<?php

include_once "api.php";
include_once "lists.php";

api("mainframe", function ($action, $parameters) {
    if ($action === "checkname") {
        if (isset($parameters->name)) {
            foreach (USERS as $user) {
                if ($user[0] === $parameters->name)
                    return [true, $user];
            }
            return [false, null];
        }
        return [false, "Missing arguments"];
    } else if ($action === "checkpassword") {
        if (isset($parameters->name) && isset($parameters->password)) {
            foreach (USERS as $user) {
                if ($user[0] === $parameters->name)
                    if ($user[1] === duth_hash($parameters->password, $user[2])) {
                        return [true, "<p>Authentication OK</p><!--For mainframe authentication use 'nc address:port/files/dumbuth/mainframe.php'-->"];
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