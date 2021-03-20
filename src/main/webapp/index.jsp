<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>菜鸟教程(runoob.com)</title>
    <script src="https://cdn.staticfile.org/jquery/2.0.0/jquery.min.js">
    </script>
    <script>
        $(document).ready(function () {

            $("#btn").click(function () {
                var p = $("p[value*='花']");
                p=$("p:contains('戏')");
                p= $("tr:contains('三解')");
                p= $("div ")

                $("td:not(:contains('hidden1'))")
                alert(p.attr("data"));
            });
        });
    </script>
</head>

<body>

<div data = "abcefeg">
    <p data = "abc"></p>
</div>

<div data = "abcefeg">
    <p data = "efg"></p>
</div>

<p data="朝花夕拾">
    <button id="btn">test</button>
</p>
<div>
    <table data = "abc">
        <tr data="good">
            <td>
                <p data = "bad">三解</p>
            </td>
        </tr>
        <tr data="now">
            <td></td>
        </tr>
    </table>
</div>
<p data="five">五猖戏</p>

<h2>这是一个标题</h2>
<p>这是一个段落。</p>
<p>这是另一个段落。</p>

<button>点我</button>

<p>     a b
    e  jgejgklglj
</p>
</body>
</html>