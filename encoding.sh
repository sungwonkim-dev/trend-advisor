FROM_ENCODING="EUC-KR"
TO_ENCODING="UTF-8"
CONVERT="iconv -c -f $FROM_ENCODING -t $TO_ENCODING"

for file in *.json; do
	$CONVERT "$file" > "${file%.json}.utf8.json"
done
exit 0
