package ru.snowadv.chat.domain.model

internal data class Emoji(
    val name: String,
    val code: Int,
) {
    fun getCodeString() = String(Character.toChars(code))
}

internal val emojiMap = listOf(

// Smileys & Emotion
    Emoji("grinning", 0x1f600),
    Emoji("smiley", 0x1f603),
    Emoji("big_smile", 0x1f604),
    Emoji("grinning_face_with_smiling_eyes", 0x1f601),
    Emoji("laughing", 0x1f606),
    Emoji("sweat_smile", 0x1f605),
    Emoji("rolling_on_the_floor_laughing", 0x1f923),
    Emoji("joy", 0x1f602),
    Emoji("smile", 0x1f642),
    Emoji("upside_down", 0x1f643),
    Emoji("wink", 0x1f609),
    Emoji("blush", 0x1f60a),
    Emoji("innocent", 0x1f607),
    Emoji("heart_eyes", 0x1f60d),
    Emoji("heart_kiss", 0x1f618),
    Emoji("kiss", 0x1f617),
    Emoji("smiling_face", 0x263a),
    Emoji("kiss_with_blush", 0x1f61a),
    Emoji("kiss_smiling_eyes", 0x1f619),
    Emoji("yum", 0x1f60b),
    Emoji("stuck_out_tongue", 0x1f61b),
    Emoji("stuck_out_tongue_wink", 0x1f61c),
    Emoji("stuck_out_tongue_closed_eyes", 0x1f61d),
    Emoji("money_face", 0x1f911),
    Emoji("hug", 0x1f917),
    Emoji("thinking", 0x1f914),
    Emoji("silence", 0x1f910),
    Emoji("neutral", 0x1f610),
    Emoji("expressionless", 0x1f611),
    Emoji("speechless", 0x1f636),
    Emoji("smirk", 0x1f60f),
    Emoji("unamused", 0x1f612),
    Emoji("rolling_eyes", 0x1f644),
    Emoji("grimacing", 0x1f62c),
    Emoji("lying", 0x1f925),
    Emoji("relieved", 0x1f60c),
    Emoji("pensive", 0x1f614),
    Emoji("sleepy", 0x1f62a),
    Emoji("drooling", 0x1f924),
    Emoji("sleeping", 0x1f634),
    Emoji("cant_talk", 0x1f637),
    Emoji("sick", 0x1f912),
    Emoji("hurt", 0x1f915),
    Emoji("nauseated", 0x1f922),
    Emoji("sneezing", 0x1f927),
    Emoji("dizzy", 0x1f635),
    Emoji("cowboy", 0x1f920),
    Emoji("sunglasses", 0x1f60e),
    Emoji("nerd", 0x1f913),
    Emoji("oh_no", 0x1f615),
    Emoji("worried", 0x1f61f),
    Emoji("frown", 0x1f641),
    Emoji("sad", 0x2639),
    Emoji("open_mouth", 0x1f62e),
    Emoji("hushed", 0x1f62f),
    Emoji("astonished", 0x1f632),
    Emoji("flushed", 0x1f633),
    Emoji("frowning", 0x1f626),
    Emoji("anguished", 0x1f627),
    Emoji("fear", 0x1f628),
    Emoji("cold_sweat", 0x1f630),
    Emoji("exhausted", 0x1f625),
    Emoji("cry", 0x1f622),
    Emoji("sob", 0x1f62d),
    Emoji("scream", 0x1f631),
    Emoji("confounded", 0x1f616),
    Emoji("persevere", 0x1f623),
    Emoji("disappointed", 0x1f61e),
    Emoji("sweat", 0x1f613),
    Emoji("weary", 0x1f629),
    Emoji("anguish", 0x1f62b),
    Emoji("triumph", 0x1f624),
    Emoji("rage", 0x1f621),
    Emoji("angry", 0x1f620),
    Emoji("smiling_devil", 0x1f608),
    Emoji("devil", 0x1f47f),
    Emoji("skull", 0x1f480),
    Emoji("skull_and_crossbones", 0x2620),
    Emoji("poop", 0x1f4a9),
    Emoji("clown", 0x1f921),
    Emoji("ogre", 0x1f479),
    Emoji("goblin", 0x1f47a),
    Emoji("ghost", 0x1f47b),
    Emoji("alien", 0x1f47d),
    Emoji("space_invader", 0x1f47e),
    Emoji("robot", 0x1f916),
    Emoji("smiley_cat", 0x1f63a),
    Emoji("smile_cat", 0x1f638),
    Emoji("joy_cat", 0x1f639),
    Emoji("heart_eyes_cat", 0x1f63b),
    Emoji("smirk_cat", 0x1f63c),
    Emoji("kissing_cat", 0x1f63d),
    Emoji("scream_cat", 0x1f640),
    Emoji("crying_cat", 0x1f63f),
    Emoji("angry_cat", 0x1f63e),
    Emoji("see_no_evil", 0x1f648),
    Emoji("hear_no_evil", 0x1f649),
    Emoji("speak_no_evil", 0x1f64a),
    Emoji("lipstick_kiss", 0x1f48b),
    Emoji("love_letter", 0x1f48c),
    Emoji("cupid", 0x1f498),
    Emoji("gift_heart", 0x1f49d),
    Emoji("sparkling_heart", 0x1f496),
    Emoji("heart_pulse", 0x1f497),
    Emoji("heartbeat", 0x1f493),
    Emoji("revolving_hearts", 0x1f49e),
    Emoji("two_hearts", 0x1f495),
    Emoji("heart_box", 0x1f49f),
    Emoji("heart_exclamation", 0x2763),
    Emoji("broken_heart", 0x1f494),
    Emoji("heart", 0x2764),
    Emoji("yellow_heart", 0x1f49b),
    Emoji("green_heart", 0x1f49a),
    Emoji("blue_heart", 0x1f499),
    Emoji("purple_heart", 0x1f49c),
    Emoji("black_heart", 0x1f5a4),
    Emoji("100", 0x1f4af),
    Emoji("anger", 0x1f4a2),
    Emoji("boom", 0x1f4a5),
    Emoji("seeing_stars", 0x1f4ab),
    Emoji("sweat_drops", 0x1f4a6),
    Emoji("dash", 0x1f4a8),
    Emoji("hole", 0x1f573),
    Emoji("bomb", 0x1f4a3),
    Emoji("umm", 0x1f4ac),
    Emoji("speech_bubble", 0x1f5e8),
    Emoji("anger_bubble", 0x1f5ef),
    Emoji("thought", 0x1f4ad),
    Emoji("zzz", 0x1f4a4),

// People & Body
    Emoji("wave", 0x1f44b),
    Emoji("stop", 0x1f91a),
    Emoji("high_five", 0x1f590),
    Emoji("hand", 0x270b),
    Emoji("spock", 0x1f596),
    Emoji("ok", 0x1f44c),
    Emoji("peace_sign", 0x270c),
    Emoji("fingers_crossed", 0x1f91e),
    Emoji("rock_on", 0x1f918),
    Emoji("call_me", 0x1f919),
    Emoji("point_left", 0x1f448),
    Emoji("point_right", 0x1f449),
    Emoji("point_up", 0x1f446),
    Emoji("middle_finger", 0x1f595),
    Emoji("point_down", 0x1f447),
    Emoji("wait_one_second", 0x261d),
    Emoji("+1", 0x1f44d),
    Emoji("-1", 0x1f44e),
    Emoji("fist", 0x270a),
    Emoji("fist_bump", 0x1f44a),
    Emoji("left_fist", 0x1f91b),
    Emoji("right_fist", 0x1f91c),
    Emoji("clap", 0x1f44f),
    Emoji("raised_hands", 0x1f64c),
    Emoji("open_hands", 0x1f450),
    Emoji("handshake", 0x1f91d),
    Emoji("pray", 0x1f64f),
    Emoji("writing", 0x270d),
    Emoji("nail_polish", 0x1f485),
    Emoji("selfie", 0x1f933),
    Emoji("muscle", 0x1f4aa),
    Emoji("ear", 0x1f442),
    Emoji("nose", 0x1f443),
    Emoji("eyes", 0x1f440),
    Emoji("eye", 0x1f441),
    Emoji("tongue", 0x1f445),
    Emoji("lips", 0x1f444),
    Emoji("baby", 0x1f476),
    Emoji("boy", 0x1f466),
    Emoji("girl", 0x1f467),
    Emoji("man", 0x1f468),
    Emoji("woman", 0x1f469),
    Emoji("older_man", 0x1f474),
    Emoji("older_woman", 0x1f475),
    Emoji("person_frowning", 0x1f64d),
    Emoji("person_pouting", 0x1f64e),
    Emoji("no_signal", 0x1f645),
    Emoji("ok_signal", 0x1f646),
    Emoji("information_desk_person", 0x1f481),
    Emoji("raising_hand", 0x1f64b),
    Emoji("bow", 0x1f647),
    Emoji("face_palm", 0x1f926),
    Emoji("shrug", 0x1f937),
    Emoji("police", 0x1f46e),
    Emoji("detective", 0x1f575),
    Emoji("guard", 0x1f482),
    Emoji("construction_worker", 0x1f477),
    Emoji("prince", 0x1f934),
    Emoji("princess", 0x1f478),
    Emoji("turban", 0x1f473),
    Emoji("gua_pi_mao", 0x1f472),
    Emoji("bride", 0x1f470),
    Emoji("pregnant", 0x1f930),
    Emoji("angel", 0x1f47c),
    Emoji("santa", 0x1f385),
    Emoji("mother_christmas", 0x1f936),
    Emoji("massage", 0x1f486),
    Emoji("haircut", 0x1f487),
    Emoji("walking", 0x1f6b6),
    Emoji("running", 0x1f3c3),
    Emoji("dancer", 0x1f483),
    Emoji("dancing", 0x1f57a),
    Emoji("levitating", 0x1f574),
    Emoji("dancers", 0x1f46f),
    Emoji("fencing", 0x1f93a),
    Emoji("horse_racing", 0x1f3c7),
    Emoji("skier", 0x26f7),
    Emoji("snowboarder", 0x1f3c2),
    Emoji("golf", 0x1f3cc),
    Emoji("surf", 0x1f3c4),
    Emoji("rowboat", 0x1f6a3),
    Emoji("swim", 0x1f3ca),
    Emoji("ball", 0x26f9),
    Emoji("lift", 0x1f3cb),
    Emoji("cyclist", 0x1f6b4),
    Emoji("mountain_biker", 0x1f6b5),
    Emoji("cartwheel", 0x1f938),
    Emoji("wrestling", 0x1f93c),
    Emoji("water_polo", 0x1f93d),
    Emoji("handball", 0x1f93e),
    Emoji("juggling", 0x1f939),
    Emoji("bath", 0x1f6c0),
    Emoji("in_bed", 0x1f6cc),
    Emoji("two_women_holding_hands", 0x1f46d),
    Emoji("man_and_woman_holding_hands", 0x1f46b),
    Emoji("two_men_holding_hands", 0x1f46c),
    Emoji("family", 0x1f46a),
    Emoji("speaking_head", 0x1f5e3),
    Emoji("silhouette", 0x1f464),
    Emoji("silhouettes", 0x1f465),
    Emoji("footprints", 0x1f463),
    Emoji("tuxedo", 0x1f935),

// Animals & Nature
    Emoji("monkey_face", 0x1f435),
    Emoji("monkey", 0x1f412),
    Emoji("gorilla", 0x1f98d),
    Emoji("puppy", 0x1f436),
    Emoji("dog", 0x1f415),
    Emoji("poodle", 0x1f429),
    Emoji("wolf", 0x1f43a),
    Emoji("fox", 0x1f98a),
    Emoji("kitten", 0x1f431),
    Emoji("cat", 0x1f408),
    Emoji("lion", 0x1f981),
    Emoji("tiger_cub", 0x1f42f),
    Emoji("tiger", 0x1f405),
    Emoji("leopard", 0x1f406),
    Emoji("pony", 0x1f434),
    Emoji("horse", 0x1f40e),
    Emoji("unicorn", 0x1f984),
    Emoji("deer", 0x1f98c),
    Emoji("calf", 0x1f42e),
    Emoji("ox", 0x1f402),
    Emoji("water_buffalo", 0x1f403),
    Emoji("cow", 0x1f404),
    Emoji("piglet", 0x1f437),
    Emoji("pig", 0x1f416),
    Emoji("boar", 0x1f417),
    Emoji("pig_nose", 0x1f43d),
    Emoji("ram", 0x1f40f),
    Emoji("sheep", 0x1f411),
    Emoji("goat", 0x1f410),
    Emoji("arabian_camel", 0x1f42a),
    Emoji("camel", 0x1f42b),
    Emoji("elephant", 0x1f418),
    Emoji("rhinoceros", 0x1f98f),
    Emoji("dormouse", 0x1f42d),
    Emoji("mouse", 0x1f401),
    Emoji("rat", 0x1f400),
    Emoji("hamster", 0x1f439),
    Emoji("bunny", 0x1f430),
    Emoji("rabbit", 0x1f407),
    Emoji("chipmunk", 0x1f43f),
    Emoji("bat", 0x1f987),
    Emoji("bear", 0x1f43b),
    Emoji("koala", 0x1f428),
    Emoji("panda", 0x1f43c),
    Emoji("paw_prints", 0x1f43e),
    Emoji("turkey", 0x1f983),
    Emoji("chicken", 0x1f414),
    Emoji("rooster", 0x1f413),
    Emoji("hatching", 0x1f423),
    Emoji("chick", 0x1f424),
    Emoji("new_baby", 0x1f425),
    Emoji("bird", 0x1f426),
    Emoji("penguin", 0x1f427),
    Emoji("dove", 0x1f54a),
    Emoji("eagle", 0x1f985),
    Emoji("duck", 0x1f986),
    Emoji("owl", 0x1f989),
    Emoji("frog", 0x1f438),
    Emoji("crocodile", 0x1f40a),
    Emoji("turtle", 0x1f422),
    Emoji("lizard", 0x1f98e),
    Emoji("snake", 0x1f40d),
    Emoji("dragon_face", 0x1f432),
    Emoji("dragon", 0x1f409),
    Emoji("whale", 0x1f433),
    Emoji("humpback_whale", 0x1f40b),
    Emoji("dolphin", 0x1f42c),
    Emoji("fish", 0x1f41f),
    Emoji("tropical_fish", 0x1f420),
    Emoji("blowfish", 0x1f421),
    Emoji("shark", 0x1f988),
    Emoji("octopus", 0x1f419),
    Emoji("shell", 0x1f41a),
    Emoji("snail", 0x1f40c),
    Emoji("butterfly", 0x1f98b),
    Emoji("bug", 0x1f41b),
    Emoji("ant", 0x1f41c),
    Emoji("bee", 0x1f41d),
    Emoji("spider", 0x1f577),
    Emoji("web", 0x1f578),
    Emoji("scorpion", 0x1f982),
    Emoji("bouquet", 0x1f490),
    Emoji("cherry_blossom", 0x1f338),
    Emoji("white_flower", 0x1f4ae),
    Emoji("rosette", 0x1f3f5),
    Emoji("rose", 0x1f339),
    Emoji("wilted_flower", 0x1f940),
    Emoji("hibiscus", 0x1f33a),
    Emoji("sunflower", 0x1f33b),
    Emoji("blossom", 0x1f33c),
    Emoji("tulip", 0x1f337),
    Emoji("seedling", 0x1f331),
    Emoji("evergreen_tree", 0x1f332),
    Emoji("tree", 0x1f333),
    Emoji("palm_tree", 0x1f334),
    Emoji("cactus", 0x1f335),
    Emoji("harvest", 0x1f33e),
    Emoji("herb", 0x1f33f),
    Emoji("shamrock", 0x2618),
    Emoji("lucky", 0x1f340),
    Emoji("maple_leaf", 0x1f341),
    Emoji("fallen_leaf", 0x1f342),
    Emoji("leaves", 0x1f343),
    Emoji("beetle", 0x1f41e),

// Food & Drink
    Emoji("grapes", 0x1f347),
    Emoji("melon", 0x1f348),
    Emoji("watermelon", 0x1f349),
    Emoji("orange", 0x1f34a),
    Emoji("lemon", 0x1f34b),
    Emoji("banana", 0x1f34c),
    Emoji("pineapple", 0x1f34d),
    Emoji("apple", 0x1f34e),
    Emoji("green_apple", 0x1f34f),
    Emoji("pear", 0x1f350),
    Emoji("peach", 0x1f351),
    Emoji("cherries", 0x1f352),
    Emoji("strawberry", 0x1f353),
    Emoji("kiwi", 0x1f95d),
    Emoji("tomato", 0x1f345),
    Emoji("avocado", 0x1f951),
    Emoji("eggplant", 0x1f346),
    Emoji("potato", 0x1f954),
    Emoji("carrot", 0x1f955),
    Emoji("corn", 0x1f33d),
    Emoji("hot_pepper", 0x1f336),
    Emoji("cucumber", 0x1f952),
    Emoji("mushroom", 0x1f344),
    Emoji("peanuts", 0x1f95c),
    Emoji("chestnut", 0x1f330),
    Emoji("bread", 0x1f35e),
    Emoji("croissant", 0x1f950),
    Emoji("baguette", 0x1f956),
    Emoji("pancakes", 0x1f95e),
    Emoji("cheese", 0x1f9c0),
    Emoji("meat", 0x1f356),
    Emoji("drumstick", 0x1f357),
    Emoji("bacon", 0x1f953),
    Emoji("hamburger", 0x1f354),
    Emoji("fries", 0x1f35f),
    Emoji("pizza", 0x1f355),
    Emoji("hotdog", 0x1f32d),
    Emoji("taco", 0x1f32e),
    Emoji("burrito", 0x1f32f),
    Emoji("doner_kebab", 0x1f959),
    Emoji("egg", 0x1f95a),
    Emoji("cooking", 0x1f373),
    Emoji("paella", 0x1f958),
    Emoji("food", 0x1f372),
    Emoji("salad", 0x1f957),
    Emoji("popcorn", 0x1f37f),
    Emoji("bento", 0x1f371),
    Emoji("senbei", 0x1f358),
    Emoji("onigiri", 0x1f359),
    Emoji("rice", 0x1f35a),
    Emoji("curry", 0x1f35b),
    Emoji("ramen", 0x1f35c),
    Emoji("spaghetti", 0x1f35d),
    Emoji("yam", 0x1f360),
    Emoji("oden", 0x1f362),
    Emoji("sushi", 0x1f363),
    Emoji("tempura", 0x1f364),
    Emoji("naruto", 0x1f365),
    Emoji("dango", 0x1f361),
    Emoji("crab", 0x1f980),
    Emoji("shrimp", 0x1f990),
    Emoji("squid", 0x1f991),
    Emoji("soft_serve", 0x1f366),
    Emoji("shaved_ice", 0x1f367),
    Emoji("ice_cream", 0x1f368),
    Emoji("donut", 0x1f369),
    Emoji("cookie", 0x1f36a),
    Emoji("birthday", 0x1f382),
    Emoji("cake", 0x1f370),
    Emoji("chocolate", 0x1f36b),
    Emoji("candy", 0x1f36c),
    Emoji("lollipop", 0x1f36d),
    Emoji("custard", 0x1f36e),
    Emoji("honey", 0x1f36f),
    Emoji("baby_bottle", 0x1f37c),
    Emoji("milk", 0x1f95b),
    Emoji("coffee", 0x2615),
    Emoji("tea", 0x1f375),
    Emoji("sake", 0x1f376),
    Emoji("champagne", 0x1f37e),
    Emoji("wine", 0x1f377),
    Emoji("cocktail", 0x1f378),
    Emoji("tropical_drink", 0x1f379),
    Emoji("beer", 0x1f37a),
    Emoji("beers", 0x1f37b),
    Emoji("clink", 0x1f942),
    Emoji("small_glass", 0x1f943),
    Emoji("hungry", 0x1f37d),
    Emoji("fork_and_knife", 0x1f374),
    Emoji("spoon", 0x1f944),
    Emoji("knife", 0x1f52a),
    Emoji("vase", 0x1f3fa),

// Activities
    Emoji("jack-o-lantern", 0x1f383),
    Emoji("holiday_tree", 0x1f384),
    Emoji("fireworks", 0x1f386),
    Emoji("sparkler", 0x1f387),
    Emoji("sparkles", 0x2728),
    Emoji("balloon", 0x1f388),
    Emoji("tada", 0x1f389),
    Emoji("confetti", 0x1f38a),
    Emoji("wish_tree", 0x1f38b),
    Emoji("bamboo", 0x1f38d),
    Emoji("dolls", 0x1f38e),
    Emoji("carp_streamer", 0x1f38f),
    Emoji("wind_chime", 0x1f390),
    Emoji("moon_ceremony", 0x1f391),
    Emoji("ribbon", 0x1f380),
    Emoji("gift", 0x1f381),
    Emoji("reminder_ribbon", 0x1f397),
    Emoji("ticket", 0x1f39f),
    Emoji("pass", 0x1f3ab),
    Emoji("military_medal", 0x1f396),
    Emoji("trophy", 0x1f3c6),
    Emoji("medal", 0x1f3c5),
    Emoji("first_place", 0x1f947),
    Emoji("second_place", 0x1f948),
    Emoji("third_place", 0x1f949),
    Emoji("football", 0x26bd),
    Emoji("baseball", 0x26be),
    Emoji("basketball", 0x1f3c0),
    Emoji("volleyball", 0x1f3d0),
    Emoji("american_football", 0x1f3c8),
    Emoji("rugby", 0x1f3c9),
    Emoji("tennis", 0x1f3be),
    Emoji("strike", 0x1f3b3),
    Emoji("cricket", 0x1f3cf),
    Emoji("field_hockey", 0x1f3d1),
    Emoji("ice_hockey", 0x1f3d2),
    Emoji("ping_pong", 0x1f3d3),
    Emoji("badminton", 0x1f3f8),
    Emoji("boxing_glove", 0x1f94a),
    Emoji("black_belt", 0x1f94b),
    Emoji("gooooooooal", 0x1f945),
    Emoji("hole_in_one", 0x26f3),
    Emoji("ice_skate", 0x26f8),
    Emoji("fishing", 0x1f3a3),
    Emoji("running_shirt", 0x1f3bd),
    Emoji("ski", 0x1f3bf),
    Emoji("direct_hit", 0x1f3af),
    Emoji("billiards", 0x1f3b1),
    Emoji("crystal_ball", 0x1f52e),
    Emoji("video_game", 0x1f3ae),
    Emoji("joystick", 0x1f579),
    Emoji("slot_machine", 0x1f3b0),
    Emoji("dice", 0x1f3b2),
    Emoji("spades", 0x2660),
    Emoji("hearts", 0x2665),
    Emoji("diamonds", 0x2666),
    Emoji("clubs", 0x2663),
    Emoji("joker", 0x1f0cf),
    Emoji("mahjong", 0x1f004),
    Emoji("playing_cards", 0x1f3b4),
    Emoji("performing_arts", 0x1f3ad),
    Emoji("picture", 0x1f5bc),
    Emoji("art", 0x1f3a8),

// Travel & Places
    Emoji("earth_africa", 0x1f30d),
    Emoji("earth_americas", 0x1f30e),
    Emoji("earth_asia", 0x1f30f),
    Emoji("www", 0x1f310),
    Emoji("map", 0x1f5fa),
    Emoji("japan", 0x1f5fe),
    Emoji("snowy_mountain", 0x1f3d4),
    Emoji("mountain", 0x26f0),
    Emoji("volcano", 0x1f30b),
    Emoji("mount_fuji", 0x1f5fb),
    Emoji("campsite", 0x1f3d5),
    Emoji("beach", 0x1f3d6),
    Emoji("desert", 0x1f3dc),
    Emoji("island", 0x1f3dd),
    Emoji("national_park", 0x1f3de),
    Emoji("stadium", 0x1f3df),
    Emoji("classical_building", 0x1f3db),
    Emoji("construction", 0x1f3d7),
    Emoji("houses", 0x1f3d8),
    Emoji("derelict_house", 0x1f3da),
    Emoji("house", 0x1f3e0),
    Emoji("suburb", 0x1f3e1),
    Emoji("office", 0x1f3e2),
    Emoji("japan_post", 0x1f3e3),
    Emoji("post_office", 0x1f3e4),
    Emoji("hospital", 0x1f3e5),
    Emoji("bank", 0x1f3e6),
    Emoji("hotel", 0x1f3e8),
    Emoji("love_hotel", 0x1f3e9),
    Emoji("convenience_store", 0x1f3ea),
    Emoji("school", 0x1f3eb),
    Emoji("department_store", 0x1f3ec),
    Emoji("factory", 0x1f3ed),
    Emoji("shiro", 0x1f3ef),
    Emoji("castle", 0x1f3f0),
    Emoji("wedding", 0x1f492),
    Emoji("tower", 0x1f5fc),
    Emoji("statue", 0x1f5fd),
    Emoji("church", 0x26ea),
    Emoji("mosque", 0x1f54c),
    Emoji("synagogue", 0x1f54d),
    Emoji("shinto_shrine", 0x26e9),
    Emoji("kaaba", 0x1f54b),
    Emoji("fountain", 0x26f2),
    Emoji("tent", 0x26fa),
    Emoji("foggy", 0x1f301),
    Emoji("night", 0x1f303),
    Emoji("city", 0x1f3d9),
    Emoji("mountain_sunrise", 0x1f304),
    Emoji("sunrise", 0x1f305),
    Emoji("sunset", 0x1f306),
    Emoji("city_sunrise", 0x1f307),
    Emoji("bridge", 0x1f309),
    Emoji("hot_springs", 0x2668),
    Emoji("carousel", 0x1f3a0),
    Emoji("ferris_wheel", 0x1f3a1),
    Emoji("roller_coaster", 0x1f3a2),
    Emoji("barber", 0x1f488),
    Emoji("circus", 0x1f3aa),
    Emoji("train", 0x1f682),
    Emoji("railway_car", 0x1f683),
    Emoji("high_speed_train", 0x1f684),
    Emoji("bullet_train", 0x1f685),
    Emoji("oncoming_train", 0x1f686),
    Emoji("subway", 0x1f687),
    Emoji("light_rail", 0x1f688),
    Emoji("station", 0x1f689),
    Emoji("oncoming_tram", 0x1f68a),
    Emoji("monorail", 0x1f69d),
    Emoji("mountain_railway", 0x1f69e),
    Emoji("tram", 0x1f68b),
    Emoji("bus", 0x1f68c),
    Emoji("oncoming_bus", 0x1f68d),
    Emoji("trolley", 0x1f68e),
    Emoji("minibus", 0x1f690),
    Emoji("ambulance", 0x1f691),
    Emoji("fire_truck", 0x1f692),
    Emoji("police_car", 0x1f693),
    Emoji("oncoming_police_car", 0x1f694),
    Emoji("taxi", 0x1f695),
    Emoji("oncoming_taxi", 0x1f696),
    Emoji("car", 0x1f697),
    Emoji("oncoming_car", 0x1f698),
    Emoji("recreational_vehicle", 0x1f699),
    Emoji("moving_truck", 0x1f69a),
    Emoji("truck", 0x1f69b),
    Emoji("tractor", 0x1f69c),
    Emoji("racecar", 0x1f3ce),
    Emoji("motorcycle", 0x1f3cd),
    Emoji("scooter", 0x1f6f5),
    Emoji("bike", 0x1f6b2),
    Emoji("kick_scooter", 0x1f6f4),
    Emoji("bus_stop", 0x1f68f),
    Emoji("road", 0x1f6e3),
    Emoji("railway_track", 0x1f6e4),
    Emoji("oil_drum", 0x1f6e2),
    Emoji("fuel_pump", 0x26fd),
    Emoji("siren", 0x1f6a8),
    Emoji("horizontal_traffic_light", 0x1f6a5),
    Emoji("traffic_light", 0x1f6a6),
    Emoji("stop_sign", 0x1f6d1),
    Emoji("work_in_progress", 0x1f6a7),
    Emoji("anchor", 0x2693),
    Emoji("boat", 0x26f5),
    Emoji("canoe", 0x1f6f6),
    Emoji("speedboat", 0x1f6a4),
    Emoji("passenger_ship", 0x1f6f3),
    Emoji("ferry", 0x26f4),
    Emoji("motor_boat", 0x1f6e5),
    Emoji("ship", 0x1f6a2),
    Emoji("airplane", 0x2708),
    Emoji("small_airplane", 0x1f6e9),
    Emoji("take_off", 0x1f6eb),
    Emoji("landing", 0x1f6ec),
    Emoji("seat", 0x1f4ba),
    Emoji("helicopter", 0x1f681),
    Emoji("suspension_railway", 0x1f69f),
    Emoji("gondola", 0x1f6a0),
    Emoji("aerial_tramway", 0x1f6a1),
    Emoji("satellite", 0x1f6f0),
    Emoji("rocket", 0x1f680),
    Emoji("bellhop_bell", 0x1f6ce),
    Emoji("times_up", 0x231b),
    Emoji("time_ticking", 0x23f3),
    Emoji("watch", 0x231a),
    Emoji("alarm_clock", 0x23f0),
    Emoji("stopwatch", 0x23f1),
    Emoji("timer", 0x23f2),
    Emoji("mantelpiece_clock", 0x1f570),
    Emoji("time", 0x1f557),
    Emoji("new_moon", 0x1f311),
    Emoji("waxing_moon", 0x1f314),
    Emoji("full_moon", 0x1f315),
    Emoji("moon", 0x1f319),
    Emoji("new_moon_face", 0x1f31a),
    Emoji("goodnight", 0x1f31b),
    Emoji("temperature", 0x1f321),
    Emoji("sunny", 0x2600),
    Emoji("moon_face", 0x1f31d),
    Emoji("sun_face", 0x1f31e),
    Emoji("star", 0x2b50),
    Emoji("glowing_star", 0x1f31f),
    Emoji("shooting_star", 0x1f320),
    Emoji("milky_way", 0x1f30c),
    Emoji("cloud", 0x2601),
    Emoji("partly_sunny", 0x26c5),
    Emoji("thunderstorm", 0x26c8),
    Emoji("mostly_sunny", 0x1f324),
    Emoji("cloudy", 0x1f325),
    Emoji("sunshowers", 0x1f326),
    Emoji("rainy", 0x1f327),
    Emoji("snowy", 0x1f328),
    Emoji("lightning", 0x1f329),
    Emoji("tornado", 0x1f32a),
    Emoji("fog", 0x1f32b),
    Emoji("windy", 0x1f32c),
    Emoji("cyclone", 0x1f300),
    Emoji("rainbow", 0x1f308),
    Emoji("closed_umbrella", 0x1f302),
    Emoji("umbrella", 0x2602),
    Emoji("umbrella_with_rain", 0x2614),
    Emoji("beach_umbrella", 0x26f1),
    Emoji("high_voltage", 0x26a1),
    Emoji("snowflake", 0x2744),
    Emoji("snowman", 0x2603),
    Emoji("frosty", 0x26c4),
    Emoji("comet", 0x2604),
    Emoji("fire", 0x1f525),
    Emoji("drop", 0x1f4a7),
    Emoji("ocean", 0x1f30a),

// Objects
    Emoji("glasses", 0x1f453),
    Emoji("dark_sunglasses", 0x1f576),
    Emoji("tie", 0x1f454),
    Emoji("shirt", 0x1f455),
    Emoji("jeans", 0x1f456),
    Emoji("dress", 0x1f457),
    Emoji("kimono", 0x1f458),
    Emoji("bikini", 0x1f459),
    Emoji("clothing", 0x1f45a),
    Emoji("purse", 0x1f45b),
    Emoji("handbag", 0x1f45c),
    Emoji("pouch", 0x1f45d),
    Emoji("shopping_bags", 0x1f6cd),
    Emoji("backpack", 0x1f392),
    Emoji("shoe", 0x1f45e),
    Emoji("athletic_shoe", 0x1f45f),
    Emoji("high_heels", 0x1f460),
    Emoji("sandal", 0x1f461),
    Emoji("boot", 0x1f462),
    Emoji("crown", 0x1f451),
    Emoji("hat", 0x1f452),
    Emoji("top_hat", 0x1f3a9),
    Emoji("graduate", 0x1f393),
    Emoji("helmet", 0x26d1),
    Emoji("prayer_beads", 0x1f4ff),
    Emoji("lipstick", 0x1f484),
    Emoji("ring", 0x1f48d),
    Emoji("gem", 0x1f48e),
    Emoji("mute", 0x1f507),
    Emoji("speaker", 0x1f508),
    Emoji("softer", 0x1f509),
    Emoji("louder", 0x1f50a),
    Emoji("loudspeaker", 0x1f4e2),
    Emoji("megaphone", 0x1f4e3),
    Emoji("horn", 0x1f4ef),
    Emoji("notifications", 0x1f514),
    Emoji("mute_notifications", 0x1f515),
    Emoji("musical_score", 0x1f3bc),
    Emoji("music", 0x1f3b5),
    Emoji("musical_notes", 0x1f3b6),
    Emoji("studio_microphone", 0x1f399),
    Emoji("volume", 0x1f39a),
    Emoji("control_knobs", 0x1f39b),
    Emoji("microphone", 0x1f3a4),
    Emoji("headphones", 0x1f3a7),
    Emoji("radio", 0x1f4fb),
    Emoji("saxophone", 0x1f3b7),
    Emoji("guitar", 0x1f3b8),
    Emoji("piano", 0x1f3b9),
    Emoji("trumpet", 0x1f3ba),
    Emoji("violin", 0x1f3bb),
    Emoji("drum", 0x1f941),
    Emoji("mobile_phone", 0x1f4f1),
    Emoji("calling", 0x1f4f2),
    Emoji("phone", 0x260e),
    Emoji("landline", 0x1f4de),
    Emoji("pager", 0x1f4df),
    Emoji("fax", 0x1f4e0),
    Emoji("battery", 0x1f50b),
    Emoji("electric_plug", 0x1f50c),
    Emoji("computer", 0x1f4bb),
    Emoji("desktop_computer", 0x1f5a5),
    Emoji("printer", 0x1f5a8),
    Emoji("keyboard", 0x2328),
    Emoji("computer_mouse", 0x1f5b1),
    Emoji("trackball", 0x1f5b2),
    Emoji("gold_record", 0x1f4bd),
    Emoji("floppy_disk", 0x1f4be),
    Emoji("cd", 0x1f4bf),
    Emoji("dvd", 0x1f4c0),
    Emoji("movie_camera", 0x1f3a5),
    Emoji("film", 0x1f39e),
    Emoji("projector", 0x1f4fd),
    Emoji("action", 0x1f3ac),
    Emoji("tv", 0x1f4fa),
    Emoji("camera", 0x1f4f7),
    Emoji("taking_a_picture", 0x1f4f8),
    Emoji("video_camera", 0x1f4f9),
    Emoji("vhs", 0x1f4fc),
    Emoji("search", 0x1f50d),
    Emoji("candle", 0x1f56f),
    Emoji("light_bulb", 0x1f4a1),
    Emoji("flashlight", 0x1f526),
    Emoji("lantern", 0x1f3ee),
    Emoji("decorative_notebook", 0x1f4d4),
    Emoji("red_book", 0x1f4d5),
    Emoji("book", 0x1f4d6),
    Emoji("green_book", 0x1f4d7),
    Emoji("blue_book", 0x1f4d8),
    Emoji("orange_book", 0x1f4d9),
    Emoji("books", 0x1f4da),
    Emoji("notebook", 0x1f4d3),
    Emoji("ledger", 0x1f4d2),
    Emoji("receipt", 0x1f4c3),
    Emoji("scroll", 0x1f4dc),
    Emoji("document", 0x1f4c4),
    Emoji("headlines", 0x1f4f0),
    Emoji("newspaper", 0x1f5de),
    Emoji("place_holder", 0x1f4d1),
    Emoji("bookmark", 0x1f516),
    Emoji("label", 0x1f3f7),
    Emoji("money", 0x1f4b0),
    Emoji("yen_banknotes", 0x1f4b4),
    Emoji("dollar_bills", 0x1f4b5),
    Emoji("euro_banknotes", 0x1f4b6),
    Emoji("pound_notes", 0x1f4b7),
    Emoji("losing_money", 0x1f4b8),
    Emoji("credit_card", 0x1f4b3),
    Emoji("stock_market", 0x1f4b9),
    Emoji("email", 0x2709),
    Emoji("e-mail", 0x1f4e7),
    Emoji("mail_received", 0x1f4e8),
    Emoji("mail_sent", 0x1f4e9),
    Emoji("outbox", 0x1f4e4),
    Emoji("inbox", 0x1f4e5),
    Emoji("package", 0x1f4e6),
    Emoji("mailbox", 0x1f4eb),
    Emoji("closed_mailbox", 0x1f4ea),
    Emoji("unread_mail", 0x1f4ec),
    Emoji("inbox_zero", 0x1f4ed),
    Emoji("mail_dropoff", 0x1f4ee),
    Emoji("ballot_box", 0x1f5f3),
    Emoji("pencil", 0x270f),
    Emoji("fountain_pen", 0x1f58b),
    Emoji("pen", 0x1f58a),
    Emoji("paintbrush", 0x1f58c),
    Emoji("crayon", 0x1f58d),
    Emoji("memo", 0x1f4dd),
    Emoji("briefcase", 0x1f4bc),
    Emoji("organize", 0x1f4c1),
    Emoji("folder", 0x1f4c2),
    Emoji("sort", 0x1f5c2),
    Emoji("calendar", 0x1f4c5),
    Emoji("date", 0x1f4c6),
    Emoji("spiral_notepad", 0x1f5d2),
    Emoji("rolodex", 0x1f4c7),
    Emoji("chart", 0x1f4c8),
    Emoji("downwards_trend", 0x1f4c9),
    Emoji("bar_chart", 0x1f4ca),
    Emoji("clipboard", 0x1f4cb),
    Emoji("push_pin", 0x1f4cc),
    Emoji("pin", 0x1f4cd),
    Emoji("paperclip", 0x1f4ce),
    Emoji("office_supplies", 0x1f587),
    Emoji("ruler", 0x1f4cf),
    Emoji("carpenter_square", 0x1f4d0),
    Emoji("scissors", 0x2702),
    Emoji("archive", 0x1f5c3),
    Emoji("file_cabinet", 0x1f5c4),
    Emoji("wastebasket", 0x1f5d1),
    Emoji("locked", 0x1f512),
    Emoji("unlocked", 0x1f513),
    Emoji("privacy", 0x1f50f),
    Emoji("secure", 0x1f510),
    Emoji("key", 0x1f511),
    Emoji("secret", 0x1f5dd),
    Emoji("hammer", 0x1f528),
    Emoji("mine", 0x26cf),
    Emoji("at_work", 0x2692),
    Emoji("working_on_it", 0x1f6e0),
    Emoji("dagger", 0x1f5e1),
    Emoji("duel", 0x2694),
    Emoji("gun", 0x1f52b),
    Emoji("bow_and_arrow", 0x1f3f9),
    Emoji("shield", 0x1f6e1),
    Emoji("fixing", 0x1f527),
    Emoji("nut_and_bolt", 0x1f529),
    Emoji("gear", 0x2699),
    Emoji("compression", 0x1f5dc),
    Emoji("justice", 0x2696),
    Emoji("link", 0x1f517),
    Emoji("chains", 0x26d3),
    Emoji("alchemy", 0x2697),
    Emoji("science", 0x1f52c),
    Emoji("telescope", 0x1f52d),
    Emoji("satellite_antenna", 0x1f4e1),
    Emoji("injection", 0x1f489),
    Emoji("medicine", 0x1f48a),
    Emoji("door", 0x1f6aa),
    Emoji("bed", 0x1f6cf),
    Emoji("living_room", 0x1f6cb),
    Emoji("toilet", 0x1f6bd),
    Emoji("shower", 0x1f6bf),
    Emoji("bathtub", 0x1f6c1),
    Emoji("shopping_cart", 0x1f6d2),
    Emoji("smoking", 0x1f6ac),
    Emoji("coffin", 0x26b0),
    Emoji("funeral_urn", 0x26b1),
    Emoji("rock_carving", 0x1f5ff),

// Symbols
    Emoji("atm", 0x1f3e7),
    Emoji("put_litter_in_its_place", 0x1f6ae),
    Emoji("potable_water", 0x1f6b0),
    Emoji("accessible", 0x267f),
    Emoji("mens", 0x1f6b9),
    Emoji("womens", 0x1f6ba),
    Emoji("restroom", 0x1f6bb),
    Emoji("baby_change_station", 0x1f6bc),
    Emoji("wc", 0x1f6be),
    Emoji("passport_control", 0x1f6c2),
    Emoji("customs", 0x1f6c3),
    Emoji("baggage_claim", 0x1f6c4),
    Emoji("locker", 0x1f6c5),
    Emoji("warning", 0x26a0),
    Emoji("children_crossing", 0x1f6b8),
    Emoji("no_entry", 0x26d4),
    Emoji("prohibited", 0x1f6ab),
    Emoji("no_bicycles", 0x1f6b3),
    Emoji("no_smoking", 0x1f6ad),
    Emoji("do_not_litter", 0x1f6af),
    Emoji("non-potable_water", 0x1f6b1),
    Emoji("no_pedestrians", 0x1f6b7),
    Emoji("no_phones", 0x1f4f5),
    Emoji("underage", 0x1f51e),
    Emoji("radioactive", 0x2622),
    Emoji("biohazard", 0x2623),
    Emoji("up", 0x2b06),
    Emoji("upper_right", 0x2197),
    Emoji("right", 0x27a1),
    Emoji("lower_right", 0x2198),
    Emoji("down", 0x2b07),
    Emoji("lower_left", 0x2199),
    Emoji("left", 0x2b05),
    Emoji("upper_left", 0x2196),
    Emoji("up_down", 0x2195),
    Emoji("left_right", 0x2194),
    Emoji("reply", 0x21a9),
    Emoji("forward", 0x21aa),
    Emoji("heading_up", 0x2934),
    Emoji("heading_down", 0x2935),
    Emoji("clockwise", 0x1f503),
    Emoji("counterclockwise", 0x1f504),
    Emoji("back", 0x1f519),
    Emoji("end", 0x1f51a),
    Emoji("on", 0x1f51b),
    Emoji("soon", 0x1f51c),
    Emoji("top", 0x1f51d),
    Emoji("place_of_worship", 0x1f6d0),
    Emoji("atom", 0x269b),
    Emoji("om", 0x1f549),
    Emoji("star_of_david", 0x2721),
    Emoji("wheel_of_dharma", 0x2638),
    Emoji("yin_yang", 0x262f),
    Emoji("cross", 0x271d),
    Emoji("orthodox_cross", 0x2626),
    Emoji("star_and_crescent", 0x262a),
    Emoji("peace", 0x262e),
    Emoji("menorah", 0x1f54e),
    Emoji("aries", 0x2648),
    Emoji("taurus", 0x2649),
    Emoji("gemini", 0x264a),
    Emoji("cancer", 0x264b),
    Emoji("leo", 0x264c),
    Emoji("virgo", 0x264d),
    Emoji("libra", 0x264e),
    Emoji("scorpius", 0x264f),
    Emoji("sagittarius", 0x2650),
    Emoji("capricorn", 0x2651),
    Emoji("aquarius", 0x2652),
    Emoji("pisces", 0x2653),
    Emoji("ophiuchus", 0x26ce),
    Emoji("shuffle", 0x1f500),
    Emoji("repeat", 0x1f501),
    Emoji("repeat_one", 0x1f502),
    Emoji("play", 0x25b6),
    Emoji("fast_forward", 0x23e9),
    Emoji("next_track", 0x23ed),
    Emoji("play_pause", 0x23ef),
    Emoji("play_reverse", 0x25c0),
    Emoji("rewind", 0x23ea),
    Emoji("previous_track", 0x23ee),
    Emoji("upvote", 0x1f53c),
    Emoji("double_up", 0x23eb),
    Emoji("downvote", 0x1f53d),
    Emoji("double_down", 0x23ec),
    Emoji("pause", 0x23f8),
    Emoji("stop_button", 0x23f9),
    Emoji("record", 0x23fa),
    Emoji("cinema", 0x1f3a6),
    Emoji("low_brightness", 0x1f505),
    Emoji("brightness", 0x1f506),
    Emoji("cell_reception", 0x1f4f6),
    Emoji("vibration_mode", 0x1f4f3),
    Emoji("phone_off", 0x1f4f4),
    Emoji("multiplication", 0x2716),
    Emoji("plus", 0x2795),
    Emoji("minus", 0x2796),
    Emoji("division", 0x2797),
    Emoji("bangbang", 0x203c),
    Emoji("interrobang", 0x2049),
    Emoji("question", 0x2753),
    Emoji("grey_question", 0x2754),
    Emoji("grey_exclamation", 0x2755),
    Emoji("exclamation", 0x2757),
    Emoji("wavy_dash", 0x3030),
    Emoji("exchange", 0x1f4b1),
    Emoji("dollars", 0x1f4b2),
    Emoji("recycle", 0x267b),
    Emoji("fleur_de_lis", 0x269c),
    Emoji("trident", 0x1f531),
    Emoji("name_badge", 0x1f4db),
    Emoji("beginner", 0x1f530),
    Emoji("circle", 0x2b55),
    Emoji("check", 0x2705),
    Emoji("checkbox", 0x2611),
    Emoji("check_mark", 0x2714),
    Emoji("cross_mark", 0x274c),
    Emoji("x", 0x274e),
    Emoji("loop", 0x27b0),
    Emoji("double_loop", 0x27bf),
    Emoji("part_alternation", 0x303d),
    Emoji("eight_spoked_asterisk", 0x2733),
    Emoji("eight_pointed_star", 0x2734),
    Emoji("sparkle", 0x2747),
    Emoji("tm", 0x2122),
    Emoji("hash", 0x0023),
    Emoji("asterisk", 0x002a),
    Emoji("zero", 0x0030),
    Emoji("one", 0x0031),
    Emoji("two", 0x0032),
    Emoji("three", 0x0033),
    Emoji("four", 0x0034),
    Emoji("five", 0x0035),
    Emoji("six", 0x0036),
    Emoji("seven", 0x0037),
    Emoji("eight", 0x0038),
    Emoji("nine", 0x0039),
    Emoji("ten", 0x1f51f),
    Emoji("capital_abcd", 0x1f520),
    Emoji("abcd", 0x1f521),
    Emoji("1234", 0x1f522),
    Emoji("symbols", 0x1f523),
    Emoji("abc", 0x1f524),
    Emoji("a", 0x1f170),
    Emoji("ab", 0x1f18e),
    Emoji("b", 0x1f171),
    Emoji("cl", 0x1f191),
    Emoji("cool", 0x1f192),
    Emoji("free", 0x1f193),
    Emoji("info", 0x2139),
    Emoji("id", 0x1f194),
    Emoji("metro", 0x24c2),
    Emoji("new", 0x1f195),
    Emoji("ng", 0x1f196),
    Emoji("o", 0x1f17e),
    Emoji("squared_ok", 0x1f197),
    Emoji("parking", 0x1f17f),
    Emoji("sos", 0x1f198),
    Emoji("squared_up", 0x1f199),
    Emoji("vs", 0x1f19a),
    Emoji("red_circle", 0x1f534),
    Emoji("blue_circle", 0x1f535),
    Emoji("black_circle", 0x26ab),
    Emoji("white_circle", 0x26aa),
    Emoji("black_large_square", 0x2b1b),
    Emoji("white_large_square", 0x2b1c),
    Emoji("black_medium_square", 0x25fc),
    Emoji("white_medium_square", 0x25fb),
    Emoji("black_medium_small_square", 0x25fe),
    Emoji("white_medium_small_square", 0x25fd),
    Emoji("black_small_square", 0x25aa),
    Emoji("white_small_square", 0x25ab),
    Emoji("large_orange_diamond", 0x1f536),
    Emoji("large_blue_diamond", 0x1f537),
    Emoji("small_orange_diamond", 0x1f538),
    Emoji("small_blue_diamond", 0x1f539),
    Emoji("red_triangle_up", 0x1f53a),
    Emoji("red_triangle_down", 0x1f53b),
    Emoji("cute", 0x1f4a0),
    Emoji("radio_button", 0x1f518),
    Emoji("black_and_white_square", 0x1f533),
    Emoji("white_and_black_square", 0x1f532),

// Flags
    Emoji("checkered_flag", 0x1f3c1),
    Emoji("triangular_flag", 0x1f6a9),
    Emoji("crossed_flags", 0x1f38c),
    Emoji("black_flag", 0x1f3f4),
    Emoji("white_flag", 0x1f3f3)
).associateBy { it.code }