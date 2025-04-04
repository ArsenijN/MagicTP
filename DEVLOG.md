# Upcoming changes:
- [ ] Allow change some settings about player disclosure visually
- [ ] Add more Forge Minecraft versions (test what versions are supported)
- [ ] Workout with Fabric
- [ ] Test out the Neoforge and Quilt
- [ ] More verbose debugging (later)
- [ ] `/rtp` with ranges (like `/rtp 12500 5` where `12500` was base coordinates and `5` was margin; margin`×100`, no floating point allowed, min `1`)
- [ ] make `/rtp` works with config for default /rtp behaviour 
- [ ] More detailed description because of accidental thing that makes "magician" mods became more "magic" (portals makes use of the "moved magically" message)
- [ ] Allow to enable and disable different parts of the MagicTP (like `/rtp` and other)
- [ ] Move handling of the `/tp` to the local

# Changes: i276
- [x] Allow to enable and disable different parts of the MagicTP (like location disclosure that didn't added previously) (change playerDisclosure in config)

# Changes: i274
- [x] Add ry_ua locale
- [x] Better message about unsupported language
- [x] Now own locales can be added to `\mods\magictp\lang\[locale_name].json`
- [x] Change locale message so it will be shown every time locale was changed to the uncompatible (and show the list of compatible like "..., compatible locales was: [en_us, uk_ua, ru_ru], change to one of them for proper message suppression. ...")

# Changes: i265
- [x] Backwards-compatibility with messageV1 type
- [x] Now "new" message type was named simply "messageV2"
- [x] Because mod has the [Modrinth page](https://modrinth.com/mod/magictp), now messageV2 displays a message about possible mod downloading for better message handling
- [x] The fabric versions will come soon, as the different Forge versions

# Changes: i249
- [x] v.2.0.0!
- [x] Message about untranslated locale and way to do (by creating issue request for adding the locale)
- [x] Allow to disable the player location disclosure
- [x] Allow to enable and disable different parts of the MagicTP (like server global messaging)
- [x] v.1.0.0 no longer supported due to fundamental changes in server-client connectivity
- [x] Test that in-game change of locale didn't break anything

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