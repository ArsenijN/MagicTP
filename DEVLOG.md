# Upcoming changes:
- [ ] Test that in-game change of locale didn't break anything
- [ ] Message about untranslated locale and way to do (by creating issue request for adding the locale)
- [ ] More verbose debugging (later)
- [ ] `/rtp` with ranges (like `/rtp 12500 5` where `12500` was base coordinates and `5` was margi; margin×100, no floating point allowed, min `1`)
- [ ] More detailed description because of accidental thing that makes "magician" mods became more "magic" (portals makes use of the "moved magically" message)
- [ ] Allow to disable the player location disclosure
- [ ] Allow to enable and disable different parts of the MagicTP (like /rtp and other)
- [ ] Move handling of the /tp to the local


# Changes: i179
- [x] Tested ru_ru locale (for correct regex usage)

# Changes: i178
- [x] Reduced debug logs about player movements (by making threshold)
- [x] UNtested ru_ru locale
- [x] Make configs (in mod menu and by `/magictp` command)
- - Commands:
- - `/magictp get [Variable]`
- - `/magictp set [Variable] [Value]`

# Changes: i149
- [x] Fix "An unexpected error was appeared"
- [x] Move regex to locales instead hard-coded `ClientChatHandler.java:19-24`
- [x] Half-coded debug (means that it wasn't full yet)

# Changes: i126
- [x] Chnaged MainMod.java to MagicTP.java
- [x] Changed main class name from MainMod to MagicTP
- [x] Corrected misspelling in `mods.toml:10`

# Changes: in chronological way, before i126
- [x] Restore the "original" source code
- [x] Restore the "original" source code
- [x] Fix unsilent messages
- [x] Rewriting the code for automatic build system
- [x] Main start