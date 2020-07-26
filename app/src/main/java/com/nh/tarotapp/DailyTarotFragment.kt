package com.nh.tarotapp

import android.app.ActionBar
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_daily_tarot.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DailyTarotFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DailyTarotFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DailyTarotFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var matches = 0
    private var dataSet : MutableList<Card> = mutableListOf<Card>()
    private var drawables : MutableList<Drawable> = mutableListOf<Drawable>()
    private var cardSelected: Card = Card(null,null, null)
    private var imageSelected: Drawable? = null
    private var cardsOfTarotReading : MutableList<Drawable?> = mutableListOf<Drawable?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_tarot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.text = "Tarot Diario"
        createDeck()

        leftCard.setOnDragListener(dragListen)
        show.setOnClickListener{
            val args = Bundle()
            args.putSerializable("cards", cardsOfTarotReading as Serializable)
            var tarotReading = TarotReadingFragment()
            tarotReading.arguments = args
            getFragmentManager()?.beginTransaction()?.replace(R.id.container, tarotReading)?.setTransition(
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN)?.commit()
        }
    }
    fun createDeck(){
        drawables
        val r = R.drawable::class.java
        val fields = r.fields
        for (field in fields){
            if(field.name.startsWith("tarot")){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Log.d("NAME",field.name);
                    drawables.add(resources.getDrawable(field.getInt(null), context?.getTheme()))
                } else {
                    drawables.add(resources.getDrawable(field.getInt(null)))
                }
            }
        }

        for (i in 0..21){
            var item = Card(drawables[i],i,getString(R.string.lorem))
            dataSet.add(item)
            val image: ImageView = ImageView(context)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                image.setImageDrawable(resources.getDrawable(R.drawable.carta,context?.theme))
            } else {
                image.setImageDrawable(resources.getDrawable(R.drawable.carta))
            }
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,10,-70,10)
            params.weight = 1.0f
            image.setLayoutParams(params)
            image.id = i
            image.setOnLongClickListener{ v:View ->
                cardSelected = item
                imageSelected = item.image
                val item = ClipData.Item(v.tag as? CharSequence)

                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    item
                )

                val myShadow = View.DragShadowBuilder(v)

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    v.startDragAndDrop(dragData,myShadow,null,0)
                }else{
                    v.startDrag(dragData,myShadow,null,0)
                }
            }
            copyDeck.addView(image)
        }

        title.text = drawables.size.toString()

    }

    val dragListen = View.OnDragListener{v, event ->
        val receiverView:ImageView = v as ImageView

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    receiverView.setBackgroundColor(Color.CYAN)
                    v.invalidate()
                    true
                } else {
                    false
                }
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                receiverView.setBackgroundColor(Color.GREEN)
                v.invalidate()
                true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
                true

            DragEvent.ACTION_DRAG_EXITED -> {
                receiverView.setBackgroundColor(Color.YELLOW)
                v.invalidate()
                true
            }

            DragEvent.ACTION_DROP -> {
                val item: ClipData.Item = event.clipData.getItemAt(0)
                val dragData = item.text
                v.invalidate()
                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                receiverView.setBackgroundColor(Color.WHITE)
                v.invalidate()

                when(event.result) {
                    true ->{

                        // drop was handled
                        receiverView.setBackgroundColor(Color.WHITE)
                        matches +=1
                        activeCards()
                    }
                    else ->{
                        // drop didn't work
                        receiverView.setBackgroundColor(Color.RED)
                    }
                }

                // returns true; the value is ignored.
                true
            }

            else -> {
                // An unknown action type was received.
                false
            }
        }
    }

    fun activeCards(){

        if(matches==1){
            leftCard.setOnDragListener(null)
            leftCard.setImageDrawable(imageSelected)
            rightCard.setOnDragListener(dragListen)
        }else if(matches==2){
            rightCard.setOnDragListener(null)
            rightCard.setImageDrawable(imageSelected)

            upCard.setOnDragListener(dragListen)
        }else if(matches==3){
            upCard.setOnDragListener(null)
            upCard.setImageDrawable(imageSelected)

            bottomCard.setOnDragListener(dragListen)

        }else if(matches==4){
            bottomCard.setOnDragListener(null)
            bottomCard.setImageDrawable(imageSelected)

            centerCard.setOnDragListener(dragListen)

        }else if(matches==5){
            centerCard.setOnDragListener(null)
            centerCard.setImageDrawable(imageSelected)

            copyDeck.visibility = View.GONE
            show.visibility = View.VISIBLE
        }
        Log.d("image",cardSelected.id.toString())
        cardsOfTarotReading.add(cardSelected.image)
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    /*;override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }*/

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DailyTarotFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DailyTarotFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
