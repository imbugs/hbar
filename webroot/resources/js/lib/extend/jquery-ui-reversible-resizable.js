$.ui.plugin.add("resizable", "alsoResizeReverse", {

  start: function() {
    var that = $(this).data( "ui-resizable" ),
      o = that.options,
      _store = function(exp) {
        $(exp).each(function() {
          var el = $(this);
          el.data("ui-resizable-alsoResizeReverse", {
            width: parseInt(el.width(), 10),
            height: parseInt(el.height(), 10),
            left: parseInt(el.css("left"), 10),
            top: parseInt(el.css("top"), 10)
          });
        });
      };

    if (typeof(o.alsoResizeReverse) === "object" && !o.alsoResizeReverse.parentNode) {
      if (o.alsoResizeReverse.length) {
        o.alsoResizeReverse = o.alsoResizeReverse[0];
        _store(o.alsoResizeReverse);
      } else {
        $.each(o.alsoResizeReverse, function(exp) {
          _store(exp);
        });
      }
    } else {
      _store(o.alsoResizeReverse);
    }
  },

  resize: function(event, ui) {
    var that = $(this).data( "ui-resizable" ),
      o = that.options,
      os = that.originalSize,
      op = that.originalPosition,
      delta = {
        height: (that.size.height - os.height - 2) || 0,
        width: (that.size.width - os.width) || 0,
        top: (that.position.top - op.top) || 0,
        left: (that.position.left - op.left) || 0
      },

      _alsoResizeReverse = function(exp, c) {
        $(exp).each(function() {
          var cssOpts = [];

          if(o.handles && (o.handles.indexOf("s") >= 0 || o.handles.indexOf("n") >= 0))
            cssOpts = cssOpts.concat(ui.originalElement[0].length ? [ "height" ] : [ "height", "top" ]);
          
          if(o.handles && (o.handles.indexOf("e") >= 0 || o.handles.indexOf("w") >= 0))
            cssOpts = cssOpts.concat(ui.originalElement[0].length ? [ "width" ] : [ "width", "left" ]);

          var el = $(this),
            start = $(this).data("ui-resizable-alsoResizeReverse"),
            style = {},
            css = c && c.length ? c : cssOpts;

          $.each(css, function(i, prop) {
            var sum = (start[prop] || 0) - (delta[prop] || 0);
            if (sum && sum >= 0) {
              style[prop] = sum || null;
            }
          });

          el.css(style);
        });
      };

    if (typeof(o.alsoResizeReverse) === "object" && !o.alsoResizeReverse.nodeType) {
      $.each(o.alsoResizeReverse, function(exp, c) {
        _alsoResizeReverse(exp, c);
      });
    } else {
      _alsoResizeReverse(o.alsoResizeReverse);
    }
  },

  stop: function() {
    $(this).removeData("resizable-alsoResizeReverse");
  }
});