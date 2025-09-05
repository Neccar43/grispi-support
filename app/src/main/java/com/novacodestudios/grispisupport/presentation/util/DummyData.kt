package com.novacodestudios.grispisupport.presentation.util

import com.novacodestudios.grispisupport.presentation.model.Application
import com.novacodestudios.grispisupport.presentation.model.Channel
import com.novacodestudios.grispisupport.presentation.model.Message
import com.novacodestudios.grispisupport.presentation.model.Notification
import com.novacodestudios.grispisupport.presentation.model.Priority
import com.novacodestudios.grispisupport.presentation.model.Tag
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.TicketEvent
import com.novacodestudios.grispisupport.presentation.model.TicketHistory
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import com.novacodestudios.grispisupport.presentation.model.Type
import com.novacodestudios.grispisupport.presentation.model.User
import com.novacodestudios.grispisupport.presentation.model.UserRole

val user1 = User("u1", "Ahmet Kuru", "ahmet@example.com", UserRole.END_USER)
val user2 = User("u2", "Mehmet Zeybek", "mehmet@zendesk.com", UserRole.AGENT, phone = "+905301234567", organization = "Grispi", groups = listOf("Destek", "Satış"))
val user3 = User("u3", "Ayşe Yılmaz", "ayse@zendesk.com", UserRole.AGENT)
val user4 = User("u4", "Zeynep Demir", "zeynep@example.com", UserRole.END_USER)
val user5 = User("u5", "Ali Sever", "ali@zendesk.com", UserRole.AGENT)

val allDummyUsers = listOf(user1, user2, user3, user4, user5)
val messagesT1 = listOf(
    Message(
        "m1",
        "t1",
        "u1",
        "Merhaba, siparişim hala elime ulaşmadı.",
        System.currentTimeMillis() - 3 * 86400000 + 3600000
    ),
    Message(
        "m2",
        "t1",
        "u2",
        "Merhaba Ahmet Bey, hemen kontrol ediyorum.",
        System.currentTimeMillis() - 3 * 86400000 + 7200000
    ),
    Message(
        "m3",
        "t1",
        "u2",
        "Kargo şirketiyle görüştüm, bugün teslim edilmesi bekleniyor.",
        System.currentTimeMillis() - 2 * 86400000 + 3600000
    ),
    Message(
        "m4",
        "t1",
        "u1",
        "Tamamdır. Halloldu.",
        System.currentTimeMillis() - 2 * 86400000 + 4600000
    ),
    Message("m5", "t1", "u1", "Teşekkürler.", System.currentTimeMillis() - 2 * 86400000 + 4700000),
    Message(
        "m6",
        "t1",
        "u2",
        "Ne demek. Biz teşekkür ederiz.",
        System.currentTimeMillis() - 2 * 86400000 + 5000000
    ),
    Message(
        "m7",
        "t1",
        "u2",
        "Bizi puanlamayı unutmayın.",
        System.currentTimeMillis() - 2 * 86400000 + 5100000
    ),
)
val messagesT2 = listOf(
    Message(
        "m4",
        "t2",
        "u4",
        "Merhaba, siparişimde yanlış ürün geldi.",
        System.currentTimeMillis() - 2 * 86400000 + 3600000
    ),
    Message(
        "m5",
        "t2",
        "u3",
        "Merhaba Zeynep Hanım, hangi ürünü bekliyordunuz?",
        System.currentTimeMillis() - 2 * 86400000 + 7200000
    ),
    Message(
        "m6",
        "t2",
        "u4",
        "Kırmızı tişört sipariş etmiştim, mavi geldi.",
        System.currentTimeMillis() - 86400000 + 3600000
    )
)
val messagesT3 = listOf(
    Message(
        "m7",
        "t3",
        "u1",
        "Merhaba, alışverişimden sonra e-fatura gelmedi.",
        System.currentTimeMillis() - 4 * 86400000 + 3600000
    ),
    Message(
        "m8",
        "t3",
        "u5",
        "Merhaba Ahmet Bey, e-faturayı sistem üzerinden yeniden gönderdim.",
        System.currentTimeMillis() - 3 * 86400000 + 7200000
    )
)
val messagesT4 = listOf(
    Message(
        "m9",
        "t4",
        "u4",
        "Merhaba, 2 gündür destek alamıyorum. Çok yavaşsınız.",
        System.currentTimeMillis() - 86400000 + 1800000
    ),
    Message(
        "m10",
        "t4",
        "u3",
        "Zeynep Hanım, gecikme için özür dileriz. Konuyu hemen inceliyoruz.",
        System.currentTimeMillis() - 86400000 + 5400000
    )
)
val messagesT5 = listOf(
    Message(
        "m11",
        "t5",
        "u1",
        "Merhaba, destek için teşekkür ederim. Sorun çözüldü.",
        System.currentTimeMillis() - 7 * 86400000 + 3600000
    ),
    Message(
        "m12",
        "t5",
        "u2",
        "Biz teşekkür ederiz Ahmet Bey. Yardımcı olabildiysek ne mutlu.",
        System.currentTimeMillis() - 6 * 86400000 + 3600000
    )
)
val dummyMessageList = messagesT1 + messagesT2 + messagesT3 + messagesT4 + messagesT5
val dummyTicketList = listOf(
    Ticket(
        id = "t1",
        number = 1001,
        subject = "Ürün teslim edilmedi",
        requester = user1,
        assignee = user2,
        followers = listOf(user3, user5),
        tags = listOf(
            Tag(1, "teslimat"), Tag(2, "acil"), Tag(3, "ödeme")
        ),
        form = "Teslimat Sorunu",
        status = TicketStatus.IN_PROGRESS,
        createdAt = System.currentTimeMillis() - 3 * 86400000,
        updatedAt = System.currentTimeMillis() - 2 * 86400000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t1" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH
    ),
    Ticket(
        id = "t2",
        number = 1002,
        subject = "Yanlış ürün gönderildi",
        requester = user4,
        assignee = user3,
        followers = listOf(user2),
        tags = listOf(
            Tag(1, "teslimat"),
        ),
        form = "Sipariş Sorunu",
        status = TicketStatus.OPEN,
        createdAt = System.currentTimeMillis() - 2 * 86400000,
        updatedAt = System.currentTimeMillis() - 86400000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t2" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH
    ),
    Ticket(
        id = "t3",
        number = 1003,
        subject = "Fatura ulaşmadı",
        requester = user1,
        assignee = user5,
        followers = listOf(),
        tags = listOf(
            Tag(1, "teslimat"),
        ),
        form = "Faturalama",
        status = TicketStatus.ON_HOLD,
        createdAt = System.currentTimeMillis() - 4 * 86400000,
        updatedAt = System.currentTimeMillis() - 3 * 86400000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t3" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH
    ),
    Ticket(
        id = "t4",
        number = 1004,
        subject = "Destek çok yavaş",
        requester = user4,
        assignee = null,
        followers = listOf(user3, user2),
        tags = listOf(
            Tag(1, "teslimat"),
        ),
        form = "Genel Destek",
        status = TicketStatus.ON_HOLD,
        createdAt = System.currentTimeMillis() - 86400000,
        updatedAt = System.currentTimeMillis() - 3600000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t4" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH
    ),
    Ticket(
        id = "t5",
        number = 1005,
        subject = "Sorun çözüldü, teşekkürler",
        requester = user1,
        assignee = user2,
        followers = listOf(user5),
        tags = listOf(
            Tag(1, "teslimat"),
        ),
        form = "Geri Bildirim",
        status = TicketStatus.RESOLVED,
        createdAt = System.currentTimeMillis() - 7 * 86400000,
        updatedAt = System.currentTimeMillis() - 6 * 86400000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t5" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH
    )
)


val currentUser = user2

val dummyNotifications = listOf(
    Notification(
        id = "n1",
        isRead = false,
        timestamp = System.currentTimeMillis() - 3600000,
        user = user1,
        ticket = dummyTicketList[0]
    ),
    Notification(
        id = "n2",
        isRead = false,
        timestamp = System.currentTimeMillis() - 7200000,
        user = user4,
        ticket = dummyTicketList[1]
    ),
    Notification(
        id = "n3",
        isRead = true,
        timestamp = System.currentTimeMillis() - 10800000,
        user = user1,
        ticket = dummyTicketList[2]
    ),
    Notification(
        id = "n4",
        isRead = true,
        timestamp = System.currentTimeMillis() - 14400000,
        user = user4,
        ticket = dummyTicketList[3]
    ),
    Notification(
        id = "n5",
        isRead = true,
        timestamp = System.currentTimeMillis() - 18000000,
        user = user1,
        ticket = dummyTicketList[4]
    ),
)

val dummyHistories = listOf(
    TicketHistory(
        id = "1",
        ticketId = "t1",
        createdAt = System.currentTimeMillis() - 3 * 86400000,
        authorId = "u2",
        events = listOf(
            TicketEvent.Comment(body = "Merhaba, siparişim hala elime ulaşmadı."),
            TicketEvent.FieldChange(fieldName = "Status", from = "Open", to = "In Progress"),
            TicketEvent.FieldChange(fieldName = "Assignee", from = null, to = "Mehmet Zeybek"),
            TicketEvent.FieldChange(fieldName = "Priority", from = "Low", to = "High"),
            TicketEvent.FieldChange(fieldName = "Tags", from = null, to = "teslimat, acil, ödeme")
        )
    ),
    TicketHistory(
        id = "3",
        ticketId = "t1",
        createdAt = System.currentTimeMillis() - 2 * 86400000,
        authorId = "u2",
        events = listOf(
            TicketEvent.Comment(body = "Kargo şirketiyle görüştüm, bugün teslim edilmesi bekleniyor."),
        )
    ),
    TicketHistory(
        id = "4",
        ticketId = "t1",
        createdAt = System.currentTimeMillis() - 2 * 86400000 + 5000000,
        authorId = "u1",
        events = listOf(
            TicketEvent.Comment(body = "Tamamdır. Halloldu."),
            TicketEvent.Comment(body = "Teşekkürler."),
        )
    ),
    TicketHistory(
        id = "2",
        ticketId = "t2",
        createdAt = System.currentTimeMillis() - 2 * 86400000,
        authorId = "u3",
        events = listOf(
            TicketEvent.Comment(body = "Merhaba, siparişimde yanlış ürün geldi."),
            TicketEvent.FieldChange(fieldName = "Status", from = "Open", to = "Pending"),
            TicketEvent.FieldChange(fieldName = "Assignee", from = null, to = "Ayşe Yılmaz")
        )
    ),

    )

val dummyApplications = listOf(
    Application(
        id = "app1",
        iconUrl = "icon_url_1",
        rating = 4.5f,
        commentsCount = 1200,
        price = "Free",
        name = "Trello",
        description = "Proje yönetim aracı"
    ),
    Application(
        id = "app2",
        iconUrl = "icon_url_2",
        rating = 4.7f,
        commentsCount = 850,
        price = "$9.99",
        name = "Slack",
        description = "İletişim ve iş birliği platformu"
    ),
    Application(
        id = "app3",
        iconUrl = "icon_url_3",
        rating = 4.3f,
        commentsCount = 430,
        price = "Free",
        name = "Zoom",
        description = "Video konferans uygulaması"
    ),
    Application(
        id = "app4",
        iconUrl = "icon_url_4",
        rating = 4.8f,
        commentsCount = 2300,
        price = "$4.99",
        name = "Evernote",
        description = "Not alma ve organizasyon aracı"
    )
)