if [ -n "$1" ]; then
    echo "-------提交分支 $1"
    git push origin $1

    echo "-------切换分支到 master"
    git checkout master

    echo "-------pull master"
    git pull origin master --allow-unrelated-histories

    echo "-------切换分支到 $1"
    git checkout $1

    echo "-------合并 master"
    git merge master

else
    echo "没有分支参数"
fi
