$('#openNewTab').on('click',function(){
    addTabs({
        id:'openNewTab' ,
        title: $(this).attr('title')?$(this).attr('title'):$(this).text(),
        content: '',
        url: './pages/tree/openInNewTab.html',
        close: true
    });
});
hideLeftMenu();