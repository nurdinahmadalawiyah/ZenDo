package com.dinzio.zendo.core.data.local

data class EmojiModel(
    val char: String,
    val label: String
)

object EmojiDataSource {
    val allEmojis = mapOf(
        "Smileys" to listOf(
            EmojiModel("😀", "grinning face happy smile"),
            EmojiModel("😁", "beaming face grin teeth"),
            EmojiModel("🤣", "rolling on the floor laughing lmfao"),
            EmojiModel("😂", "face with tears of joy funny"),
            EmojiModel("🙂", "slightly smiling face calm"),
            EmojiModel("🙃", "upside down face sarcasm"),
            EmojiModel("😉", "winking face playful"),
            EmojiModel("😊", "smiling face blush shy"),
            EmojiModel("🥲", "smiling face with tear proud emotional"),
            EmojiModel("🥳", "partying face celebration birthday"),
            EmojiModel("😎", "smiling face with sunglasses cool"),
            EmojiModel("🤓", "nerd face geek smart"),
            EmojiModel("🤨", "face with raised eyebrow suspicious"),
            EmojiModel("😐", "neutral face meh"),
            EmojiModel("😴", "sleeping face tired zzz"),
            EmojiModel("🤯", "exploding head mind blown"),
            EmojiModel("🥺", "pleading face puppy eyes"),
            EmojiModel("🫠", "melting face exhausted"),
            EmojiModel("🫡", "saluting face respect"),
            EmojiModel("💀", "skull dead cringe"),
            EmojiModel("🔥", "fire lit trending")
        ),

        "Tasks & Study" to listOf(
            EmojiModel("🚀", "rocket launch start fast"),
            EmojiModel("🎯", "bullseye target focus goal"),
            EmojiModel("📚", "books study learn"),
            EmojiModel("📖", "open book reading"),
            EmojiModel("✍️", "writing hand notes"),
            EmojiModel("📝", "memo writing task"),
            EmojiModel("💻", "laptop coding work"),
            EmojiModel("🖥️", "desktop computer workstation"),
            EmojiModel("🧠", "brain thinking focus"),
            EmojiModel("💡", "light bulb idea insight"),
            EmojiModel("⏰", "alarm clock deadline"),
            EmojiModel("⏳", "hourglass time running"),
            EmojiModel("📅", "calendar schedule"),
            EmojiModel("📊", "bar chart analytics progress"),
            EmojiModel("✅", "check mark done completed")
        ),

        "Activities" to listOf(
            EmojiModel("🏃", "running exercise cardio"),
            EmojiModel("🧘", "meditation yoga calm"),
            EmojiModel("🏋️", "gym lifting weights"),
            EmojiModel("🚴", "cycling bike sport"),
            EmojiModel("🎨", "painting art hobby"),
            EmojiModel("🎸", "guitar music"),
            EmojiModel("🎧", "headphone music focus"),
            EmojiModel("🎮", "gaming console play"),
            EmojiModel("🏀", "basketball sport"),
            EmojiModel("⚽", "soccer football"),
            EmojiModel("🏸", "badminton sport"),
            EmojiModel("🏊", "swimming pool"),
            EmojiModel("🧗", "climbing challenge")
        ),

        "Food & Drink" to listOf(
            EmojiModel("☕", "coffee cafe morning"),
            EmojiModel("🍵", "tea matcha"),
            EmojiModel("🥐", "croissant breakfast"),
            EmojiModel("🍳", "cooking breakfast"),
            EmojiModel("🍔", "burger fast food"),
            EmojiModel("🍕", "pizza"),
            EmojiModel("🍜", "noodles ramen"),
            EmojiModel("🍚", "rice meal"),
            EmojiModel("🍰", "cake dessert"),
            EmojiModel("🍩", "donut sweet"),
            EmojiModel("🥤", "drink soda"),
            EmojiModel("🍺", "beer chill"),
            EmojiModel("🍷", "wine relax")
        ),

        "Nature" to listOf(
            EmojiModel("🌤️", "sun behind cloud weather"),
            EmojiModel("🌧️", "rain weather gloomy"),
            EmojiModel("🌙", "moon night"),
            EmojiModel("⭐", "star favorite"),
            EmojiModel("🌱", "seedling grow progress"),
            EmojiModel("🌳", "tree nature"),
            EmojiModel("🌸", "flower spring"),
            EmojiModel("🔥", "fire energy"),
            EmojiModel("🌊", "wave ocean"),
            EmojiModel("🐶", "dog pet"),
            EmojiModel("🐱", "cat pet"),
            EmojiModel("🦁", "lion brave"),
            EmojiModel("🐼", "panda cute")
        ),

        "Objects" to listOf(
            EmojiModel("📱", "smartphone mobile"),
            EmojiModel("🔋", "battery power energy"),
            EmojiModel("🔌", "plug charging"),
            EmojiModel("💾", "floppy disk save"),
            EmojiModel("🔑", "key security"),
            EmojiModel("🛡️", "shield protection"),
            EmojiModel("💰", "money cash"),
            EmojiModel("💳", "credit card payment"),
            EmojiModel("🎁", "gift reward"),
            EmojiModel("📦", "package delivery"),
            EmojiModel("🧳", "luggage travel"),
            EmojiModel("⚙️", "gear setting"),
            EmojiModel("🧩", "puzzle problem solving")
        ),

        "Urban Meme" to listOf(
            EmojiModel("🗿", "moai stone face deadpan meme"),
            EmojiModel("🫥", "dotted line face invisible awkward"),
            EmojiModel("🫣", "face with peeking eye cringe nervous"),
            EmojiModel("🤡", "clown makeup self clown"),
            EmojiModel("😬", "grimacing face awkward yikes"),
            EmojiModel("🙄", "face with rolling eyes annoyed"),
            EmojiModel("😮‍💨", "face exhaling tired fed up"),
            EmojiModel("😵‍💫", "face with spiral eyes confused overwhelmed"),
            EmojiModel("🫶", "heart hands fake love support"),
            EmojiModel("💅", "nail polish slay sass confident"),
            EmojiModel("✨", "sparkles aesthetic delulu"),
            EmojiModel("👀", "eyes lurking watching tea"),
            EmojiModel("🫵", "pointing at you caught"),
            EmojiModel("📉", "chart decreasing mental health stonks"),
            EmojiModel("📈", "chart increasing glow up stonks"),
            EmojiModel("🧍", "person standing awkward silence"),
            EmojiModel("🪑", "chair sitting waiting tea"),
            EmojiModel("🧎", "kneeling begging desperate"),
            EmojiModel("🫠", "melting face mentally exhausted"),
            EmojiModel("🧃", "juice box cope comfort"),
            EmojiModel("🫡", "salute respect sarcastic"),
            EmojiModel("💀", "skull dead from laughter"),
            EmojiModel("🔥", "fire slaps hard"),
            EmojiModel("🧠", "brain galaxy brain moment"),
            EmojiModel("🤝", "handshake corporate agreement deal")
        )
    )
}