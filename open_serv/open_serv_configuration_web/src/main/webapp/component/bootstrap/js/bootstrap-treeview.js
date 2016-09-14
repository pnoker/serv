/* =========================================================
 * bootstrap-treeview.js v1.0.2
 * =========================================================
 * Copyright 2013 Jonathan Miles 
 * Project URL : http://www.jondmiles.com/bootstrap-treeview
 *	
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================= */

;(function($, window, document, undefined) {

	/*global jQuery, console*/

	'use strict';

	var pluginName = 'treeview';

	var Tree = function(element, options) {

		this.$element = $(element);
		this._element = element;
		this._elementId = this._element.id;
		this._styleId = this._elementId + '-style';

		this.tree = [];
		this.nodes = [];
		this.selectedNode = {};
		this.checkedNode = {};
		
		this._init(options);
	};

	Tree.defaults = {

		injectStyle: true,

		levels: 2,

		expandIcon: 'glyphicon glyphicon-plus',
		collapseIcon: 'glyphicon glyphicon-minus',
		emptyIcon: 'glyphicon',
		nodeIcon: 'glyphicon glyphicon-stop',

		color: undefined, // '#000000',
		backColor: undefined, // '#FFFFFF',
		borderColor: undefined, // '#dddddd',
		onhoverColor: '#F5F5F5',
		selectedColor: '#FFFFFF',
		selectedBackColor: '#428bca',

		enableLinks: false,
		highlightSelected: true,
		showBorder: true,
		showTags: false,
		hasCheckbox:false,
		clickable:true,
		onNodeCheckedCallback:undefined,//选中复选框后的回调函数
		onNodeChecked:undefined,//选中复选框时调用的函数
		onNodeExpanded:undefined,//节点展开或收起后调用的函数


		// Event handler for when a node is selected
		onNodeSelected: undefined
	};

	Tree.prototype = {

		remove: function() {

			this._destroy();
			$.removeData(this, 'plugin_' + pluginName);
			$('#' + this._styleId).remove();
		},

		_destroy: function() {

			if (this.initialized) {
				this.$wrapper.remove();
				this.$wrapper = null;

				// Switch off events
				this._unsubscribeEvents();
			}

			// Reset initialized flag
			this.initialized = false;
		},

		_init: function(options) {
		
			if (options.data) {
				if (typeof options.data === 'string') {
					options.data = $.parseJSON(options.data);
				}
				this.tree = $.extend(true, [], options.data);
				delete options.data;
			}

			this.options = $.extend({}, Tree.defaults, options);

			this._setInitialLevels(this.tree, 0);

			this._destroy();
			this._subscribeEvents();
			this._render();
		},

		_unsubscribeEvents: function() {

			this.$element.off('click');

			if (typeof (this.options.onNodeSelected) === 'function') {
				this.$element.off('nodeSelected');
			}

		},

		_subscribeEvents: function() {

			this._unsubscribeEvents();

			if(!this.options.clickable){
				this.$element.on('click','input',{'thisObj':this},this._setCheckedNode);
				this.$element.on('click','.expand-collapse',{'thisObj':this},this._clickHandler);
			}else{
				this.$element.on('click',$.proxy(this._clickHandler, this));
			}


			if (typeof (this.options.onNodeSelected) === 'function') {
				this.$element.on('nodeSelected', this.options.onNodeSelected);
			}
		},

		_clickHandler: function(event) {

			var _this;

			if(this.options == undefined){
				_this = event.data.thisObj;
			}else{
				_this = this;
			}

			if (!_this.options.enableLinks) { event.preventDefault(); }
			
			var target = $(event.target),
				classList = target.attr('class') ? target.attr('class').split(' ') : [],
				node = _this._findNode(target);

			if ((classList.indexOf('click-expand') != -1) ||
					(classList.indexOf('click-collapse') != -1)) {
				// Expand or collapse node by toggling child node visibility
				_this._toggleNodes(node);
				_this._render();
				if (typeof (_this.options.onNodeExpanded) === 'function') {
					_this.options.onNodeExpanded();
				}
			}
			else if (node) {
				if (_this._isSelectable(node)) {
					_this._setSelectedNode(node);
				} else {
					_this._toggleNodes(node);
					_this._render();
				}
			}
		},

		// Looks up the DOM for the closest parent list item to retrieve the 
		// data attribute nodeid, which is used to lookup the node in the flattened structure.
		_findNode: function(target) {

			var nodeId = target.closest('li.list-group-item').attr('data-nodeid'),
				node = this.nodes[nodeId];

			if (!node) {
				console.log('Error: node does not exist');
			}
			return node;
		},

		// Actually triggers the nodeSelected event
		_triggerNodeSelectedEvent: function(node) {

			this.$element.trigger('nodeSelected', [$.extend(true, {}, node)]);
		},


		// Handles selecting and unselecting of nodes, 
		// as well as determining whether or not to trigger the nodeSelected event
		_setSelectedNode: function(node) {

			if (!node) { return; }
			
			/*if (node === this.selectedNode) {
				this.selectedNode = {};
			}
			else {
				this._triggerNodeSelectedEvent(this.selectedNode = node);
			}*/
			this._triggerNodeSelectedEvent(this.selectedNode = node);

			if(this.options.clickable){
				this._render();
			}
		},

		_setCheckedNode:function(event){

			var _this = event.data.thisObj;
			var target = $(event.target),checked = false,node = _this._findNode(target);

			var index = $(this).attr('index');
			if($(this).attr('checked')){
				node.checkboxes[index].checked = true;
				checked = true;
			}else{
				node.checkboxes[index].checked = false;
				checked = false;
			}

			_this._checkChildrenNode(node,checked,index,this);
			_this._checkParentNode(node,checked,index,this);
			if(typeof (_this.options.onNodeCheckedCallback) === 'function'){
				_this.options.onNodeCheckedCallback(event,node);
			}
		},

		_checkParentNode:function(nodeChild,checked,index,checkboxChild){
			var isCheckParent = true;
			var _this = this;
			var parentId = nodeChild.pid;
			var currentNode = $('#'+_this._elementId).find('li[id="'+nodeChild.id+'"]');
			var parentNode = $('#'+_this._elementId).find('li[id="'+nodeChild.pid+'"]');
			var parentNodeParam = _this.nodes[parentNode.attr('data-nodeid')];
			//父节点存在且父节点有复选框时，才执行以下操作
			if(parentNode.length != 0 && parentNodeParam != undefined && parentNodeParam.checkboxes != undefined){
				//判断同级元素是否与当前复选框选中状态一致
				currentNode.siblings().find('input[index="'+index+'"][pid="'+parentId+'"]').each(function(){
					var currChecked = $(this).attr('checked');
					var flag = currChecked == 'checked'?true:false;
					if(flag != checked){
						isCheckParent = false;//该值为false，表示不一致
					}
				});
				//如当前节点同级兄弟节点均为选中状态，或不是所有节点均为选中状态时，将父节点的复选框状态置为与当前节点一致
				if((isCheckParent&&checked) || (!isCheckParent&&!checked) || currentNode.siblings().find('input[index="'+index+'"][pid="'+parentId+'"]').length == 0){
					parentNodeParam.checkboxes[index].checked = checked;
					currentNode.siblings('li[id="'+parentId+'"]').find('input[index="'+index+'"]').attr('checked',checked);
				}
				checked = currentNode.find('input[index="'+index+'"]').attr('checked') == 'checked'?true:false;
				_this._checkParentNode(parentNodeParam,checked,index,checkboxChild);
			}
		},

		_checkChildrenNode:function(nodeParent,checked,index,checkboxParent){

			var _this = this;
			//当前节点未展开
			if(nodeParent._nodes){
				//迭代未展开的子节点，并将复选框选中
				$.each(nodeParent._nodes,function(i,node){
					node.checkboxes[index].checked = checked;
					_this._checkChildrenNode(node,checked,index,checkboxParent);
				});
			}else if(nodeParent.nodes){
				//当前节点已展开
				var currCheckbox = $(checkboxParent);
				//迭代已展开的子节点
				$.each(nodeParent.nodes,function(i,node){
					node.checkboxes[index].checked = checked;//将复选框的checked值设置为与父级一样的值
					var currCheckbox = $('li[id="'+node.id+'"]').find('input[index="'+index+'"]');

					currCheckbox.attr('checked',checked);//为当前节点设置复选框选中的效果
					_this._checkChildrenNode(node,checked,index,currCheckbox);

				});
			}

		},

		// On initialization recurses the entire tree structure 
		// setting expanded / collapsed states based on initial levels
		_setInitialLevels: function(nodes, level) {

			if (!nodes) { return; }
			level += 1;

			var self = this;
			$.each(nodes, function addNodes(id, node) {
				
				if (level >= self.options.levels) {
					self._toggleNodes(node);
				}

				// Need to traverse both nodes and _nodes to ensure 
				// all levels collapsed beyond levels
				var nodes = node.nodes ? node.nodes : node._nodes ? node._nodes : undefined;
				if (nodes) {
					return self._setInitialLevels(nodes, level);
				}
			});
		},

		// Toggle renaming nodes -> _nodes, _nodes -> nodes
		// to simulate expanding or collapsing a node.
		_toggleNodes: function(node) {

			if (!node.nodes && !node._nodes) {
				return;
			}

			if (node.nodes) {
				node._nodes = node.nodes;
				delete node.nodes;
			}
			else {
				node.nodes = node._nodes;
				delete node._nodes;
			}
		},

		// Returns true if the node is selectable in the tree
		_isSelectable: function (node) {
			return node.selectable !== false;
		},

		_render: function() {

			var self = this;

			if (!self.initialized) {

				// Setup first time only components
				self.$element.addClass(pluginName);
				self.$wrapper = $(self._template.list);

				self._injectStyle();
				
				self.initialized = true;
			}

			self.$element.empty().append(self.$wrapper.empty());

			// Build tree
			self.nodes = [];
			self._buildTree(self.tree, 0);
		},

		// Starting from the root node, and recursing down the 
		// structure we build the tree one node at a time
		_buildTree: function(nodes, level) {

			if (!nodes) { return; }
			level += 1;

			var self = this;
			$.each(nodes, function addNodes(id, node) {

				node.nodeId = self.nodes.length;
				self.nodes.push(node);

				//var treeItem = $(self._template.item)
				var treeItem = $('<li class="list-group-item" level="'+node.level+'"></li>')
					.addClass('node-' + self._elementId)
					.addClass((node === self.selectedNode) ? 'node-selected' : '')
					.attr('data-nodeid', node.nodeId)
					.attr('id', node.id)
					.attr('style', self._buildStyleOverride(node));

				// Add indent/spacer to mimic tree structure
				for (var i = 0; i < (level - 1); i++) {
					treeItem.append(self._template.indent);
				}

				// Add expand, collapse or empty spacer icons 
				// to facilitate tree structure navigation
				if (node._nodes) {
					treeItem
						.append($(self._template.expandCollapseIcon)
							.addClass('click-expand')
							.addClass(self.options.expandIcon)
						);
				}
				else if (node.nodes) {
					treeItem
						.append($(self._template.expandCollapseIcon)
							.addClass('click-collapse')
							.addClass(self.options.collapseIcon)
						);
				}
				else {
					treeItem
						.append($(self._template.expandCollapseIcon)
							.addClass(self.options.emptyIcon)
						);
				}

				// Add node icon
				treeItem
					.append($(self._template.icon)
						.addClass(node.icon ? node.icon : self.options.nodeIcon)
					);

				// Add text
				if (self.options.enableLinks) {
					// Add hyperlink
					treeItem
						.append($(self._template.link)
							.attr('href', node.href)
							.append(node.text)
						);
				}
				else {
					// otherwise just text
					treeItem
						.append(node.text);
				}


				//add checkboxes
				if(self.options.hasCheckbox && node.checkboxes){
					$.each(node.checkboxes, function addCheckbox(id, checkbox) {

						if(checkbox.checked){
							treeItem.append($(self._template.checkboxItem).append(checkbox.name)).
								append('<input class="checkbox-item" ' +
								'index="'+checkbox.index+'" id='+checkbox.id+' dataId='+checkbox.dataId+' pid='+checkbox.pid+' checked="checked" type="checkbox" ' +
								'style="float: right;position: relative;z-index: 2;">');
						}else{
							treeItem.append($(self._template.checkboxItem).append(checkbox.name)).
								append('<input class="checkbox-item" ' +
								'index="'+checkbox.index+'" id='+checkbox.id+' pid='+checkbox.pid+' dataId='+checkbox.dataId+' type="checkbox" ' +
								'style="float: right;position: relative;z-index: 2;">');
						}

						//treeItem.append($(self._template.checkbox).append($(self._template.checkboxItem).append(checkbox)));
					});
				}

				// Add tags as badges
				if (self.options.showTags && node.tags) {
					$.each(node.tags, function addTag(id, tag) {
						treeItem
							.append($(self._template.badge)
								.append(tag)
							);
					});
				}

				// Add item to the tree
				self.$wrapper.append(treeItem);

				// Recursively add child ndoes
				if (node.nodes) {
					return self._buildTree(node.nodes, level);
				}
			});
		},

		// Define any node level style override for
		// 1. selectedNode
		// 2. node|data assigned color overrides
		_buildStyleOverride: function(node) {

			var style = '';
			if (this.options.highlightSelected && (node === this.selectedNode)) {
				style += 'color:' + this.options.selectedColor + ';';
			}
			else if (node.color) {
				style += 'color:' + node.color + ';';
			}

			if (this.options.highlightSelected && (node === this.selectedNode)) {
				style += 'background-color:' + this.options.selectedBackColor + ';';
			}
			else if (node.backColor) {
				style += 'background-color:' + node.backColor + ';';
			}

			return style;
		},

		// Add inline style into head 
		_injectStyle: function() {

			if (this.options.injectStyle && !document.getElementById(this._styleId)) {
				$('<style type="text/css" id="' + this._styleId + '"> ' + this._buildStyle() + ' </style>').appendTo('head');
			}
		},

		// Construct trees style based on user options
		_buildStyle: function() {

			var style = '.node-' + this._elementId + '{';
			if (this.options.color) {
				style += 'color:' + this.options.color + ';';
			}
			if (this.options.backColor) {
				style += 'background-color:' + this.options.backColor + ';';
			}
			if (!this.options.showBorder) {
				style += 'border:none;';
			}
			else if (this.options.borderColor) {
				style += 'border:1px solid ' + this.options.borderColor + ';';
			}
			style += '}';

			if (this.options.onhoverColor) {
				style += '.node-' + this._elementId + ':hover{' +
				'background-color:' + this.options.onhoverColor + ';' +
				'}';
			}

			return this._css + style;
		},

		_template: {
			list: '<ul class="list-group"></ul>',
			//item: '<li class="list-group-item"></li>',
			indent: '<span class="indent"></span>',
			expandCollapseIcon: '<span class="expand-collapse"></span>',
			icon: '<span class="icon"></span>',
			link: '<a href="#" style="color:inherit;"></a>',
			badge: '<span class="badge"></span>',
			checkboxItem:'<span style="float: right;margin-right: 15%"></span>'
		},

		_css: '.treeview .list-group-item{cursor:pointer}.treeview span.indent{margin-left:10px;margin-right:10px}.treeview span.expand-collapse{width:1rem;height:1rem}.treeview span.icon{margin-left:10px;margin-right:5px}'
		// _css: '.list-group-item{cursor:pointer;}.list-group-item:hover{background-color:#f5f5f5;}span.indent{margin-left:10px;margin-right:10px}span.icon{margin-right:5px}'

	};

	var logError = function(message) {
        if(window.console) {
            window.console.error(message);
        }
    };

	// Prevent against multiple instantiations,
	// handle updates and method calls
	$.fn[pluginName] = function(options, args) {
		return this.each(function() {
			var self = $.data(this, 'plugin_' + pluginName);
			if (typeof options === 'string') {
				if (!self) {
					logError('Not initialized, can not call method : ' + options);
				}
				else if (!$.isFunction(self[options]) || options.charAt(0) === '_') {
					logError('No such method : ' + options);
				}
				else {
					if (typeof args === 'string') {
						args = [args];
					}
					self[options].apply(self, args);
				}
			}
			else {
				if (!self) {
					$.data(this, 'plugin_' + pluginName, new Tree(this, $.extend(true, {}, options)));
				}
				else {
					self._init(options);
				}
			}
		});
	};

})(jQuery, window, document);


