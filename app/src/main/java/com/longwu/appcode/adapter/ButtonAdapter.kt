package com.longwu.appcode.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.longwu.appcode.R
import kotlinx.android.synthetic.main.activity_main_button.view.button
import kotlin.random.Random

class ButtonAdapter(private val context: Context, private val buttonList: List<ButtonItem>) :
    RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_main_button, parent, false)
        return ButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val buttonItem = buttonList[position]
        holder.bind(buttonItem)
    }

    override fun getItemCount(): Int {
        return buttonList.size
    }

    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(buttonItem: ButtonItem) {
            itemView.button.text = buttonItem.title
            val background = ContextCompat.getDrawable(context, R.drawable.rounded_button_background) as GradientDrawable
            background.setColor(generateColorGradient()) // 动态设置背景颜色

            itemView.button.background = background
            itemView.button.setOnClickListener {
                buttonItem.clickAction()
            }
        }

        // 使用HSV模型生成渐变色背景
        private fun generateColorGradient(): Int {
            val baseHue = 250f  // 固定色调值，如蓝色基调，范围是0-360
            val randomSaturation = Random.nextFloat() * 0.5f + 0.5f // 饱和度在50%到100%之间
            val randomValue = Random.nextFloat() * 0.4f + 0.6f // 亮度在70%到100%之间，避免过暗或过亮
            return Color.HSVToColor(floatArrayOf(baseHue, randomSaturation, randomValue))
        }
    }
}

data class ButtonItem(val title: String, val clickAction: () -> Unit)