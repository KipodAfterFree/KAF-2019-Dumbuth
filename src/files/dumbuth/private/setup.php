<?php

$users = [];
array_push($users, duth_user_create("nobody", random(6), random(32)));
array_push($users, duth_user_create("admin", random(6), random(32)));
file_put_contents(__DIR__ . DIRECTORY_SEPARATOR . "users.json", json_encode($users));

function duth_user_create($name, $password, $salt)
{
    $user = [];
    echo "$name, $password\n";
    array_push($user, $name);
    array_push($user, duth_hash($password, $salt));
    array_push($user, $salt);
    return $user;
}

function random($length)
{
    $current = str_shuffle("abcdefghijklmnopqrstuvwxyz")[0];
    if ($length > 0) {
        return $current . random($length - 1);
    }
    return "";
}

function duth_hash($secret, $salt, $rounds = 10)
{
    if ($rounds === 0)
        return sha1($secret . $salt);
    return sha1($salt . duth_hash($secret, $salt, $rounds - 1));
}