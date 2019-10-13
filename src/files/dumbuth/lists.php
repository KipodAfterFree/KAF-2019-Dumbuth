<?php

const USERS = [
    ["nobody","9g5cfiz0ie5jm9blecrc7mioiq9tdx1a","9g5cfiz0ie5jm9blecrc7mioiq9tdx1a"],
    ["admin","c1985afb0bb63da9929ca455e8f5edc8b5adafc4", "uibhubc5v09e0txg56hgf5srxw3geob6"]
];

function duth_hash($secret, $salt, $rounds = 100)
{
    if ($rounds === 0)
        return sha1($secret . $salt);
    return sha1($salt . duth_hash($secret, $salt, $rounds - 1));
}