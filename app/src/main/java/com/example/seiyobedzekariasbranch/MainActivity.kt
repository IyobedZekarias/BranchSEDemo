package com.example.seiyobedzekariasbranch

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.seiyobedzekariasbranch.ui.theme.SEIyobedZekariasBranchTheme
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchLinkCreateListener
import io.branch.referral.Branch.sessionBuilder
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.*
import io.branch.referral.validators.IntegrationValidator
import org.json.JSONObject
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onStart() {
        super.onStart()
        /*Branch.sessionBuilder(this).withCallback { branchUniversalObject, linkProperties, error ->
            if (error != null) {
                Log.e("BranchSDK_Tester", "branch init failed. Caused by -" + error.message)
            } else {
                Log.e("BranchSDK_Tester", "branch init complete!")
                if (branchUniversalObject != null) {
                    Log.e("BranchSDK_Tester", "title " + branchUniversalObject.title)
                    Log.e("BranchSDK_Tester", "CanonicalIdentifier " + branchUniversalObject.canonicalIdentifier)
                    Log.e("BranchSDK_Tester", "metadata " + branchUniversalObject.contentMetadata.convertToJson())
                }
                if (linkProperties != null) {
                    Log.e("BranchSDK_Tester", "Channel " + linkProperties.channel)
                    Log.e("BranchSDK_Tester", "control params " + linkProperties.controlParams)
                }
            }
        }.withData(this.intent.data).init()

         */


        Branch.sessionBuilder(this).withCallback(object : Branch.BranchReferralInitListener {
            override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
                if (error == null) {
                    Log.e("BRANCH SDK", referringParams.toString())
                    //read_values = referringParams.toString()
                } else {
                    Log.e("BRANCH SDK2", error.message)
                }
            }
        }).withData(this.intent.data).init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        sessionBuilder(this).withCallback { referringParams, error ->
            if (error != null) {
                Log.e("BranchSDK_Tester", error.message)
            } else if (referringParams != null) {
                Log.e("BranchSDK_Tester", referringParams.toString())
            }
        }.reInit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val buo = BranchUniversalObject()
            .setCanonicalIdentifier("content/12345")
            .setTitle("My Content Title")
            .setContentDescription("My Content Description")
            .setContentImageUrl("https://firebasestorage.googleapis.com/v0/b/musicapp-15f17.appspot.com/o/album_art%2FVexento%2FGon.jpg?alt=media&token=33851cc7-9cfd-4766-8460-deaa9d782cc7")
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setContentMetadata(ContentMetadata().addCustomMetadata("Sent_By", "Iyobed Zekarias"))

        val lp = LinkProperties()
            .setChannel("facebook")
            .setFeature("sharing")
            .setCampaign("content 123 launch")
            .setStage("new user")
            .addControlParameter("Sent_By", "Iyobed Zekarias")

        val shareSheetStyle = ShareSheetStyle(
            this,
            "Your Awesome Deal",
            "You will never believe what happened next!"
        )
            .setCopyUrlStyle(
                resources.getDrawable(R.drawable.ic_menu_send),
                "Copy",
                "Added to clipboard"
            )
            .setMoreOptionStyle(resources.getDrawable(R.drawable.ic_menu_search), "Show more")
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
            .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
            .setAsFullWidthStyle(true)
            .setSharingTitle("Share With")






        setContent {
            SEIyobedZekariasBranchTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxWidth()) {
                    MyComposable(buo = buo, lp = lp)
                }
            }
        }
    }
}

@Composable
private fun MyComposable(modifier: Modifier = Modifier, buo: BranchUniversalObject, lp: LinkProperties) {
    var link_toshare by remember { mutableStateOf("Click a button") }
    BoxWithConstraints(modifier = modifier.fillMaxWidth(1f)) {

        Log.e("IyobedSDK", "Testing that theis works")
        var read_values = ""
        val activity = LocalContext.current as Activity



        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        val topHeight: Dp = maxHeight * 2 / 3
        val bottomHeight: Dp = maxHeight / 3

        val centerHeight = 100.dp

        val centerPaddingBottom = bottomHeight - centerHeight / 2



        Column(modifier = Modifier
            .background(Color.Blue)
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .height(topHeight)) {

        }



        Row(modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .height(bottomHeight)) {
            Button(
                modifier = Modifier.padding(top = 150.dp, start = 3.dp).width(maxWidth/4 - 5.dp),
                onClick = {
                    Log.e("button", "tester")
                    buo.generateShortUrl(activity, lp, BranchLinkCreateListener { url, error ->
                        Log.e("BRANCH SDK", "got my Branch link to share: " + url)
                        link_toshare = url

                    })
                }) {
                Text("Create Link")
            }

            Button(
                modifier = Modifier.padding(top = 150.dp, start = 3.dp).width(maxWidth/4 - 5.dp),
                onClick = {
                    if (Branch.getInstance().latestReferringParams.isNull("Sent_By")) {
                        link_toshare = "You didn't open a link to get here"
                    } else {
                        link_toshare =
                            "You were sent by " + Branch.getInstance().latestReferringParams["Sent_By"].toString()
                    }
                }) {
                Text("Read Params")
            }

            Button(
                modifier = Modifier.padding(top = 150.dp, start = 3.dp).width(maxWidth/4),
                onClick = {
                    BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                        .setAffiliation("affiliate program 1")
                        .setCustomerEventAlias("purchasing")
                        .setCoupon("123456")
                        .setCurrency(CurrencyType.USD)
                        .setDescription("Customer purchased item")
                        .setShipping(0.0)
                        .setTax(9.75)
                        .setRevenue(1.5)
                        .addCustomDataProperty("Purchasing Some Item", "From Store")
                        .addContentItems(buo)
                        .logEvent(activity.applicationContext)

                    link_toshare = "logged purchase of item as Branch Event"
                }) {
                Text("Purchase Event")
            }

            Button (
                modifier = Modifier.padding(top = 150.dp, start = 3.dp).width(maxWidth/4 - 5.dp),
            onClick = {
                BranchEvent("Hire Iyobed ")
                    .addCustomDataProperty("Hiring Iyobed", "Of course")
                    .addCustomDataProperty("Custom Hiring Event", "1")
                    .setCustomerEventAlias("hire Iyobed")
                    .logEvent(activity.applicationContext);

                link_toshare = "logged hiring Branch Custom Event"
            }){
                Text("Hire Iyobed as Full time")
            }
        }


        Column(modifier = Modifier
            .background(Color.Red, shape = RoundedCornerShape(3.dp))
            .padding(start = 10.dp, end = 10.dp, bottom = centerPaddingBottom)
            .width(centerHeight * 3)
            .align(Alignment.Center)) {
            SelectionContainer(modifier = Modifier.padding(top = 20.dp)) {
                Text(link_toshare, color=Color.White, textAlign = TextAlign.Center)
            }

        }


    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SEIyobedZekariasBranchTheme {
        //MyComposable()
    }
}