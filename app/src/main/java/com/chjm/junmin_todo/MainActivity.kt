package com.chjm.junmin_todo

import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chjm.junmin_todo.databinding.ActivityMainBinding
import com.chjm.junmin_todo.databinding.ItemTodoBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)




        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = TodoAdapter(
                emptyList(),
                onClickDeleteIcon = {
//                    deleteTodo(it)
                    viewModel.deleteTodo(it)

                },
                onClickItem = {
//                    toggleTodo(it)
                    viewModel.toggleTodo(it)
                }

            )
        }

        binding.addButton.setOnClickListener {
            val todo = Todo(binding.editTextTextPersonName.text.toString())
//            addTodo(todo)

            viewModel.addTodo(todo)

        }

        // 관찰 UI 업데이트
        viewModel.todoLiveData.observe(this, Observer {
            (binding.recyclerView.adapter as TodoAdapter).setData(it)
        })
    }

//    private fun toggleTodo(todo: Todo) {
//        todo.isDone = !todo.isDone
//        binding.recyclerView.adapter?.notifyDataSetChanged()
//    }
//
//    private fun addTodo() {
//        val todo = Todo(binding.editTextTextPersonName.text.toString())
//        data.add(todo)
//        binding.recyclerView.adapter?.notifyDataSetChanged()
//    }
//
//    private fun deleteTodo(todo: Todo) {
//        data.remove(todo)
//        binding.recyclerView.adapter?.notifyDataSetChanged()
//    }
}


data class Todo(
    val text: String, var isDone: Boolean = false
)


class TodoAdapter(
    private var myDataset: List<Todo>,
    val onClickDeleteIcon: (todo: Todo) -> Unit,
    val onClickItem: (todo: Todo) -> Unit
) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {


    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoAdapter.TodoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)


        return TodoViewHolder(ItemTodoBinding.bind(view))
    }


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val todo = myDataset[position]
        holder.binding.todoText.text = todo.text


        if (todo.isDone) {


            holder.binding.todoText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }

        } else {

            holder.binding.todoText.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }

        }

        holder.binding.deleteImageView.setOnClickListener {

            onClickDeleteIcon.invoke(todo)

        }

        holder.binding.root.setOnClickListener {
            onClickItem.invoke(todo)
        }

    }


    override fun getItemCount() = myDataset.size

    fun setData(newData: List<Todo>) {
        myDataset = newData
        notifyDataSetChanged()
    }
}


class MainViewModel : ViewModel() {

    val todoLiveData = MutableLiveData<List<Todo>>()

    private val data = arrayListOf<Todo>()


    fun toggleTodo(todo: Todo) {
        todo.isDone = !todo.isDone
        todoLiveData.value = data

    }

    fun addTodo(todo: Todo) {
        data.add(todo)
        todoLiveData.value = data
    }

    fun deleteTodo(todo: Todo) {
        data.remove(todo)
        todoLiveData.value = data

    }

}