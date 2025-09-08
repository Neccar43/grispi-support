package com.novacodestudios.grispisupport.presentation.util

import com.novacodestudios.grispisupport.presentation.detail.component.FieldResponse
import com.novacodestudios.grispisupport.presentation.detail.component.FieldType
import com.novacodestudios.grispisupport.presentation.detail.component.Form
import com.novacodestudios.grispisupport.presentation.detail.component.FormField
import com.novacodestudios.grispisupport.presentation.detail.component.FormResponse
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
val user2 = User(
    "u2",
    "Mehmet Zeybek",
    "mehmet@zendesk.com",
    UserRole.AGENT,
    phone = "+905301234567",
    organization = "Grispi",
    groups = listOf("Destek", "Satış")
)
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
        formId = "delivery_form",
        status = TicketStatus.OPEN,
        createdAt = System.currentTimeMillis() - 3 * 86400000,
        updatedAt = System.currentTimeMillis() - 2 * 86400000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t1" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH,
        formResponseId = "fr2"
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
        formId = "default_form",
        status = TicketStatus.PENDING,
        createdAt = System.currentTimeMillis() - 2 * 86400000,
        updatedAt = System.currentTimeMillis() - 86400000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t2" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH,
        formResponseId = "fr3"
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
        formId = "membership_form",
        status = TicketStatus.ON_HOLD,
        createdAt = System.currentTimeMillis() - 4 * 86400000,
        updatedAt = System.currentTimeMillis() - 3 * 86400000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t3" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH,
        formResponseId = "fr1"
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
        formId = "default_form",
        status = TicketStatus.ON_HOLD,
        createdAt = System.currentTimeMillis() - 86400000,
        updatedAt = System.currentTimeMillis() - 3600000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t4" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH,
        formResponseId = ""
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
        formId = "default_form",
        status = TicketStatus.RESOLVED,
        createdAt = System.currentTimeMillis() - 7 * 86400000,
        updatedAt = System.currentTimeMillis() - 6 * 86400000,
        lastMessageContent = dummyMessageList.last { it.ticketId == "t5" }.content,
        channel = Channel.WHATSAPP,
        type = Type.QUESTION,
        priority = Priority.HIGH,
        formResponseId = ""
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


val dummyForms = listOf(
    Form(
        id = "default_form",
        name = "Default Form",
        fields = listOf(
            FormField(
                id = "tags",
                label = "Etiketler",
                type = FieldType.MULTI_SELECT,
                options = listOf("Önemli", "Finans", "Teknik"),
                required = false,
            ),
            FormField(
                id = "type",
                label = "Tür",
                type = FieldType.SINGLE_SELECT,
                options = listOf("Soru", "Olay", "Problem", "Görev"),
                required = true,
            )
        )
    ),
    Form(
        id = "membership_form",
        name = "Üyelik Hesap Formu",
        fields = listOf(
            FormField(
                id = "tags",
                label = "Etiketler",
                type = FieldType.MULTI_SELECT,
                options = listOf("Hesap", "Şifre", "Destek"),
                required = false,
            ),
            FormField(
                id = "username",
                label = "Üye Kullanıcı Adı",
                type = FieldType.TEXT,
                required = true,
            ),
            FormField(
                id = "next_call_date",
                label = "Sonraki Arama Tarihi",
                type = FieldType.DATE,
                required = false,
            ),
            FormField(
                id = "request_type",
                label = "Kullanıcı Talep Tipi",
                type = FieldType.SINGLE_SELECT,
                options = listOf("Şifremi Unuttum", "Hesabıma Giriş Yapamıyorum", "Diğer"),
                required = true,
            )
        )
    ),
    Form(
        id = "delivery_form",
        name = "Teslimat ve Kargo Formu",
        fields = listOf(
            FormField(
                id = "sender",
                label = "Gönderici Kurum Adı",
                type = FieldType.TEXT,
                required = true,
            ),
            FormField(
                id = "send_to_backoffice",
                label = "Backoffice Kontrolüne Gönderilecek",
                type = FieldType.CHECKBOX,
                required = false,
            ),
            FormField(
                id = "order_status",
                label = "Sipariş Durumu",
                type = FieldType.SINGLE_SELECT,
                options = listOf("Hazırlanıyor", "Yolda", "Teslim Edildi", "İade Edildi"),
                required = true,
            ),
            FormField(
                id = "tracking_code",
                label = "Kargo Takip Kodu",
                type = FieldType.TEXT,
                required = false,
            ),
            FormField(
                id = "origin_branch",
                label = "Başlangıç Şubesi",
                type = FieldType.TEXT,
                required = false,
            ),
            FormField(
                id = "destination_branch",
                label = "Varış Şubesi",
                type = FieldType.TEXT,
                required = false,
                // value = "Ankara"
            ),
            FormField(
                id = "driver_name",
                label = "Sürücü Adı",
                type = FieldType.TEXT,
                required = false,
                // value = "Ahmet Yılmaz"
            ),
            FormField(
                id = "order_number",
                label = "Sipariş Numarası",
                type = FieldType.TEXT,
                required = false,
                // value = "ORD-20250905"
            ),
            FormField(
                id = "shipping_date",
                label = "Ürünün Kargoya Verilme Tarihi",
                type = FieldType.DATE,
                required = false,
                // value = "2025-09-05"
            )
        )
    )
)

val dummyFormResponses = listOf(
    FormResponse(
        id = "fr1",
        formId = "membership_form",
        ticketId = "t3",
        responses = listOf(
            FieldResponse(fieldId = "tags", value = listOf("Hesap", "Şifre")),
            FieldResponse(fieldId = "username", value = listOf("ahmet kuru")),
            FieldResponse(fieldId = "next_call_date", value = listOf("10.09.2025")),
            FieldResponse(fieldId = "request_type", value = listOf("Şifremi Unuttum"))
        )
    ),
    FormResponse(
        id = "fr2",
        formId = "delivery_form",
        ticketId = "t1",
        responses = listOf(
            FieldResponse(fieldId = "tags", value = listOf("teslimat", "acil", "ödeme")),
            FieldResponse(fieldId = "sender", value = listOf("Trendyol")),
            FieldResponse(fieldId = "send_to_backoffice", value = listOf("true")),
            FieldResponse(fieldId = "order_status", value = listOf("Yolda")),
            FieldResponse(fieldId = "tracking_code", value = listOf("TR123456789")),
            FieldResponse(fieldId = "origin_branch", value = listOf("İstanbul")),
            FieldResponse(fieldId = "destination_branch", value = listOf("Ankara")),
            FieldResponse(fieldId = "driver_name", value = listOf("Ahmet Yılmaz")),
            FieldResponse(fieldId = "order_number", value = listOf("ORD-20250905")),
            FieldResponse(fieldId = "shipping_date", value = listOf("05.09.2025"))
        )
    ),
    FormResponse(
        id = "fr3",
        formId = "default_form",
        ticketId = "t2",
        responses = listOf(
            FieldResponse(fieldId = "tags", value = listOf("teslimat")),
            FieldResponse(fieldId = "type", value = listOf("Soru"))
        )

    )
)
