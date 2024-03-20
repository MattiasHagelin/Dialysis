package com.math3249.dialysis.util


import android.content.Intent
import android.net.Uri
import android.text.Html

class EmailManager {

    fun validateEmail(email: String): Boolean {
        //TODO validate a email address
        return false
    }

    fun sendEmail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setDataAndType(Uri.parse("mailto:"), "text/html")
        intent.putExtra(Intent.EXTRA_EMAIL, "mattias1985@gmail.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello")
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<p>Hello</p>", 0))
//        startActivity(intent)
//        try {
//
//            val stringSenderEmail = "mattias1985@mail.com"
//            val stringReceiverEmail = "mattias1985@mail.com"
//            val stringPasswordSenderEmail = "ukbg rtil plhm pnjh"
//
//            val stringHost = "smtp.gmail.com"
//
//            val properties: Properties = System.getProperties()
//
//            properties.setProperty("mail.transport.protocol", "smtp")
//            properties.setProperty("mail.host", stringHost)
//            properties["mail.smtp.host"] = stringHost
//            properties["mail.smtp.port"] = "465"
//            properties["mail.smtp.socketFactory.fallback"] = "false"
//            properties.setProperty("mail.smtp.quitwait", "false")
//            properties["mail.smtp.socketFactory.port"] = "465"
//            properties["mail.smtp.starttls.enable"] = "true"
//            properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
//            properties["mail.smtp.ssl.enable"] = "true"
//            properties["mail.smtp.auth"] = "true"
//
//            val session: Session = Session.getInstance(properties, object : Authenticator() {
//                override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
//                    return javax.mail.PasswordAuthentication(
//                        stringSenderEmail,
//                        stringPasswordSenderEmail
//                    )
//                }
//            })
//
//            val address = InternetAddress(stringReceiverEmail)
//            val mimeMessage = MimeMessage(session)
//            mimeMessage.addRecipient(Message.RecipientType.TO, address)
//
//            mimeMessage.subject = "Subject: Android App email"
//            mimeMessage.setText("Hello Programmer, \n\nProgrammer World has sent you this 2nd email. \n\n Cheers!\nProgrammer World")
//
//            val thread = Thread {
//                try {
//                    Transport.send(mimeMessage)
//                } catch (e: MessagingException) {
//                    e.printStackTrace()
//                }
//            }
//            thread.start()
//        } catch (e: AddressException) {
//            e.printStackTrace()
//        } catch (e: MessagingException) {
//            e.printStackTrace()
//        }
    }

}