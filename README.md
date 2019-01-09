# Note-Keeper
A simple note keeping app based on MVVM architecture - Room, LiveData, ViewModel, ListAdapter.

It has simple features like adding new notes, updating existing notes and delete notes by swiping them off.
I have used the best android practice to make this app. It can be used for learning the basic concepts of Model-View-ViewModel (MVVM) architecture.
I have mostly used Androidx libraries. 

Flow of App -
Note.java - Model class or Entity for the app's database. Three fields - Title, Description and Priority.
NoteDao.java - Data Access Object for Room database. Methods - Insert, Update, Delete, DeleteAll, GetAllNotes.
NoteDatabase.java - Main database class that connects DAO and Room.
NoteRepository.class - Not a part of MVVM, but it's the best practise. Used to provide an abstraction layer between ViewModel and all the data sources.
NoteViewModel.class - ViewModel class that provides data to UI components of the app.
NoteAdapter.class - ListAdapter class for RecyclerView 
MainActivity.class - Launcher class that shows the list of notes.
AddEditNoteActivity.class - Handles adding and editing notes.
