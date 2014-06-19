// automatically generated, do not modify

#include "flatbuffers/flatbuffers.h"

namespace mupdf {

enum {
  PathCommand_MOVETO = 0,
  PathCommand_LINETO = 1,
  PathCommand_CURVETO = 2,
  PathCommand_CLOSE = 3,
};

inline const char **EnumNamesPathCommand() {
  static const char *names[] = { "MOVETO", "LINETO", "CURVETO", "CLOSE", nullptr };
  return names;
}

inline const char *EnumNamePathCommand(int e) { return EnumNamesPathCommand()[e]; }

enum {
  ShadeType_FUNCTION = 0,
  ShadeType_AXIAL = 1,
  ShadeType_RADIAL = 2,
  ShadeType_FREE_FORM = 3,
  ShadeType_LATTICE = 4,
  ShadeType_COONS_PATCH_MESH = 5,
  ShadeType_TENSOR_PRODUCTS_MESH = 6,
};

inline const char **EnumNamesShadeType() {
  static const char *names[] = { "FUNCTION", "AXIAL", "RADIAL", "FREE_FORM", "LATTICE", "COONS_PATCH_MESH", "TENSOR_PRODUCTS_MESH", nullptr };
  return names;
}

inline const char *EnumNameShadeType(int e) { return EnumNamesShadeType()[e]; }

enum {
  DisplayCommand_BEGIN_PAGE = 0,
  DisplayCommand_END_PAGE = 1,
  DisplayCommand_FILL_PATH = 2,
  DisplayCommand_STROKE_PATH = 3,
  DisplayCommand_CLIP_PATH = 4,
  DisplayCommand_CLIP_STROKE_PATH = 5,
  DisplayCommand_FILL_TEXT = 6,
  DisplayCommand_STROKE_TEXT = 7,
  DisplayCommand_CLIP_TEXT = 8,
  DisplayCommand_CLIP_STROKE_TEXT = 9,
  DisplayCommand_IGNORE_TEXT = 10,
  DisplayCommand_FILL_SHADE = 11,
  DisplayCommand_FILL_IMAGE = 12,
  DisplayCommand_FILL_IMAGE_MASK = 13,
  DisplayCommand_CLIP_IMAGE_MASK = 14,
  DisplayCommand_POP_CLIP = 15,
  DisplayCommand_BEGIN_MASK = 16,
  DisplayCommand_END_MASK = 17,
  DisplayCommand_BEGIN_GROUP = 18,
  DisplayCommand_END_GROUP = 19,
  DisplayCommand_BEGIN_TILE = 20,
  DisplayCommand_END_TILE = 21,
};

inline const char **EnumNamesDisplayCommand() {
  static const char *names[] = { "BEGIN_PAGE", "END_PAGE", "FILL_PATH", "STROKE_PATH", "CLIP_PATH", "CLIP_STROKE_PATH", "FILL_TEXT", "STROKE_TEXT", "CLIP_TEXT", "CLIP_STROKE_TEXT", "IGNORE_TEXT", "FILL_SHADE", "FILL_IMAGE", "FILL_IMAGE_MASK", "CLIP_IMAGE_MASK", "POP_CLIP", "BEGIN_MASK", "END_MASK", "BEGIN_GROUP", "END_GROUP", "BEGIN_TILE", "END_TILE", nullptr };
  return names;
}

inline const char *EnumNameDisplayCommand(int e) { return EnumNamesDisplayCommand()[e]; }

enum {
  DisplayListItem_NONE = 0,
  DisplayListItem_Path = 1,
  DisplayListItem_Text = 2,
  DisplayListItem_Shade = 3,
  DisplayListItem_Image = 4,
  DisplayListItem_BlendMode = 5,
};

inline const char **EnumNamesDisplayListItem() {
  static const char *names[] = { "NONE", "Path", "Text", "Shade", "Image", "BlendMode", nullptr };
  return names;
}

inline const char *EnumNameDisplayListItem(int e) { return EnumNamesDisplayListItem()[e]; }

enum {
  DisplayListNodeFlags_ISOLATED = 1,
  DisplayListNodeFlags_KNOCKOUT = 2,
};

inline const char **EnumNamesDisplayListNodeFlags() {
  static const char *names[] = { "ISOLATED", "KNOCKOUT", nullptr };
  return names;
}

inline const char *EnumNameDisplayListNodeFlags(int e) { return EnumNamesDisplayListNodeFlags()[e - DisplayListNodeFlags_ISOLATED]; }

enum {
  LineCap_BUTT = 0,
  LineCap_ROUND = 1,
  LineCap_SQUARE = 2,
  LineCap_TRIANGLE = 3,
};

inline const char **EnumNamesLineCap() {
  static const char *names[] = { "BUTT", "ROUND", "SQUARE", "TRIANGLE", nullptr };
  return names;
}

inline const char *EnumNameLineCap(int e) { return EnumNamesLineCap()[e]; }

enum {
  LineJoin_MITER = 0,
  LineJoin_ROUND = 1,
  LineJoin_BEVEL = 2,
};

inline const char **EnumNamesLineJoin() {
  static const char *names[] = { "MITER", "ROUND", "BEVEL", nullptr };
  return names;
}

inline const char *EnumNameLineJoin(int e) { return EnumNamesLineJoin()[e]; }

struct Rect;
struct Point;
struct Matrix;
struct PathNode;
struct Path;
struct TextNode;
struct Text;
struct Shade;
struct Image;
struct BlendMode;
struct StrokeState;
struct DisplayListNode;
struct DisplayList;

MANUALLY_ALIGNED_STRUCT(4) Rect {
 private:
  float x0_;
  float y0_;
  float x1_;
  float y1_;

 public:
  Rect(float x0, float y0, float x1, float y1)
    : x0_(flatbuffers::EndianScalar(x0)), y0_(flatbuffers::EndianScalar(y0)), x1_(flatbuffers::EndianScalar(x1)), y1_(flatbuffers::EndianScalar(y1)) {}

  float x0() const { return flatbuffers::EndianScalar(x0_); }
  float y0() const { return flatbuffers::EndianScalar(y0_); }
  float x1() const { return flatbuffers::EndianScalar(x1_); }
  float y1() const { return flatbuffers::EndianScalar(y1_); }
};
STRUCT_END(Rect, 16);

MANUALLY_ALIGNED_STRUCT(4) Point {
 private:
  float x_;
  float y_;

 public:
  Point(float x, float y)
    : x_(flatbuffers::EndianScalar(x)), y_(flatbuffers::EndianScalar(y)) {}

  float x() const { return flatbuffers::EndianScalar(x_); }
  float y() const { return flatbuffers::EndianScalar(y_); }
};
STRUCT_END(Point, 8);

MANUALLY_ALIGNED_STRUCT(4) Matrix {
 private:
  float a_;
  float b_;
  float c_;
  float d_;
  float e_;
  float f_;

 public:
  Matrix(float a, float b, float c, float d, float e, float f)
    : a_(flatbuffers::EndianScalar(a)), b_(flatbuffers::EndianScalar(b)), c_(flatbuffers::EndianScalar(c)), d_(flatbuffers::EndianScalar(d)), e_(flatbuffers::EndianScalar(e)), f_(flatbuffers::EndianScalar(f)) {}

  float a() const { return flatbuffers::EndianScalar(a_); }
  float b() const { return flatbuffers::EndianScalar(b_); }
  float c() const { return flatbuffers::EndianScalar(c_); }
  float d() const { return flatbuffers::EndianScalar(d_); }
  float e() const { return flatbuffers::EndianScalar(e_); }
  float f() const { return flatbuffers::EndianScalar(f_); }
};
STRUCT_END(Matrix, 24);

struct PathNode : private flatbuffers::Table {
  uint8_t cmd() const { return GetField<uint8_t>(4, 0); }
  const Point *coord() const { return GetStruct<const Point *>(6); }
};

struct PathNodeBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_cmd(uint8_t cmd) { fbb_.AddElement<uint8_t>(4, cmd, 0); }
  void add_coord(const Point *coord) { fbb_.AddStruct(6, coord); }
  PathNodeBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<PathNode> Finish() { return flatbuffers::Offset<PathNode>(fbb_.EndTable(start_, 2)); }
};

inline flatbuffers::Offset<PathNode> CreatePathNode(flatbuffers::FlatBufferBuilder &_fbb, uint8_t cmd, const Point *coord) {
  PathNodeBuilder builder_(_fbb);
  builder_.add_coord(coord);
  builder_.add_cmd(cmd);
  return builder_.Finish();
}

struct Path : private flatbuffers::Table {
  const Point *begin() const { return GetStruct<const Point *>(4); }
  const flatbuffers::Vector<flatbuffers::Offset<PathNode>> *nodes() const { return GetPointer<const flatbuffers::Vector<flatbuffers::Offset<PathNode>> *>(6); }
};

struct PathBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_begin(const Point *begin) { fbb_.AddStruct(4, begin); }
  void add_nodes(flatbuffers::Offset<flatbuffers::Vector<flatbuffers::Offset<PathNode>>> nodes) { fbb_.AddOffset(6, nodes); }
  PathBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<Path> Finish() { return flatbuffers::Offset<Path>(fbb_.EndTable(start_, 2)); }
};

inline flatbuffers::Offset<Path> CreatePath(flatbuffers::FlatBufferBuilder &_fbb, const Point *begin, flatbuffers::Offset<flatbuffers::Vector<flatbuffers::Offset<PathNode>>> nodes) {
  PathBuilder builder_(_fbb);
  builder_.add_nodes(nodes);
  builder_.add_begin(begin);
  return builder_.Finish();
}

struct TextNode : private flatbuffers::Table {
  const Point *translate() const { return GetStruct<const Point *>(4); }
  int32_t gid() const { return GetField<int32_t>(6, 0); }
  int32_t ucs() const { return GetField<int32_t>(8, 0); }
};

struct TextNodeBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_translate(const Point *translate) { fbb_.AddStruct(4, translate); }
  void add_gid(int32_t gid) { fbb_.AddElement<int32_t>(6, gid, 0); }
  void add_ucs(int32_t ucs) { fbb_.AddElement<int32_t>(8, ucs, 0); }
  TextNodeBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<TextNode> Finish() { return flatbuffers::Offset<TextNode>(fbb_.EndTable(start_, 3)); }
};

inline flatbuffers::Offset<TextNode> CreateTextNode(flatbuffers::FlatBufferBuilder &_fbb, const Point *translate, int32_t gid, int32_t ucs) {
  TextNodeBuilder builder_(_fbb);
  builder_.add_ucs(ucs);
  builder_.add_gid(gid);
  builder_.add_translate(translate);
  return builder_.Finish();
}

struct Text : private flatbuffers::Table {
  int32_t font() const { return GetField<int32_t>(4, 0); }
  const Matrix *trm() const { return GetStruct<const Matrix *>(6); }
  const flatbuffers::Vector<flatbuffers::Offset<TextNode>> *nodes() const { return GetPointer<const flatbuffers::Vector<flatbuffers::Offset<TextNode>> *>(8); }
};

struct TextBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_font(int32_t font) { fbb_.AddElement<int32_t>(4, font, 0); }
  void add_trm(const Matrix *trm) { fbb_.AddStruct(6, trm); }
  void add_nodes(flatbuffers::Offset<flatbuffers::Vector<flatbuffers::Offset<TextNode>>> nodes) { fbb_.AddOffset(8, nodes); }
  TextBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<Text> Finish() { return flatbuffers::Offset<Text>(fbb_.EndTable(start_, 3)); }
};

inline flatbuffers::Offset<Text> CreateText(flatbuffers::FlatBufferBuilder &_fbb, int32_t font, const Matrix *trm, flatbuffers::Offset<flatbuffers::Vector<flatbuffers::Offset<TextNode>>> nodes) {
  TextBuilder builder_(_fbb);
  builder_.add_nodes(nodes);
  builder_.add_trm(trm);
  builder_.add_font(font);
  return builder_.Finish();
}

struct Shade : private flatbuffers::Table {
  uint8_t type() const { return GetField<uint8_t>(4, 0); }
  const Rect *bbox() const { return GetStruct<const Rect *>(6); }
  const Matrix *matrix() const { return GetStruct<const Matrix *>(8); }
  int32_t useBackground() const { return GetField<int32_t>(10, 0); }
  const flatbuffers::Vector<float> *background() const { return GetPointer<const flatbuffers::Vector<float> *>(12); }
  int32_t useFunction() const { return GetField<int32_t>(14, 0); }
  const flatbuffers::Vector<float> *function() const { return GetPointer<const flatbuffers::Vector<float> *>(16); }
  const flatbuffers::Vector<int32_t> *extend() const { return GetPointer<const flatbuffers::Vector<int32_t> *>(18); }
  const flatbuffers::Vector<float> *coords() const { return GetPointer<const flatbuffers::Vector<float> *>(20); }
};

struct ShadeBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_type(uint8_t type) { fbb_.AddElement<uint8_t>(4, type, 0); }
  void add_bbox(const Rect *bbox) { fbb_.AddStruct(6, bbox); }
  void add_matrix(const Matrix *matrix) { fbb_.AddStruct(8, matrix); }
  void add_useBackground(int32_t useBackground) { fbb_.AddElement<int32_t>(10, useBackground, 0); }
  void add_background(flatbuffers::Offset<flatbuffers::Vector<float>> background) { fbb_.AddOffset(12, background); }
  void add_useFunction(int32_t useFunction) { fbb_.AddElement<int32_t>(14, useFunction, 0); }
  void add_function(flatbuffers::Offset<flatbuffers::Vector<float>> function) { fbb_.AddOffset(16, function); }
  void add_extend(flatbuffers::Offset<flatbuffers::Vector<int32_t>> extend) { fbb_.AddOffset(18, extend); }
  void add_coords(flatbuffers::Offset<flatbuffers::Vector<float>> coords) { fbb_.AddOffset(20, coords); }
  ShadeBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<Shade> Finish() { return flatbuffers::Offset<Shade>(fbb_.EndTable(start_, 9)); }
};

inline flatbuffers::Offset<Shade> CreateShade(flatbuffers::FlatBufferBuilder &_fbb, uint8_t type, const Rect *bbox, const Matrix *matrix, int32_t useBackground, flatbuffers::Offset<flatbuffers::Vector<float>> background, int32_t useFunction, flatbuffers::Offset<flatbuffers::Vector<float>> function, flatbuffers::Offset<flatbuffers::Vector<int32_t>> extend, flatbuffers::Offset<flatbuffers::Vector<float>> coords) {
  ShadeBuilder builder_(_fbb);
  builder_.add_coords(coords);
  builder_.add_extend(extend);
  builder_.add_function(function);
  builder_.add_useFunction(useFunction);
  builder_.add_background(background);
  builder_.add_useBackground(useBackground);
  builder_.add_matrix(matrix);
  builder_.add_bbox(bbox);
  builder_.add_type(type);
  return builder_.Finish();
}

struct Image : private flatbuffers::Table {
  uint32_t id() const { return GetField<uint32_t>(4, 0); }
  uint32_t maskId() const { return GetField<uint32_t>(6, 0); }
};

struct ImageBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_id(uint32_t id) { fbb_.AddElement<uint32_t>(4, id, 0); }
  void add_maskId(uint32_t maskId) { fbb_.AddElement<uint32_t>(6, maskId, 0); }
  ImageBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<Image> Finish() { return flatbuffers::Offset<Image>(fbb_.EndTable(start_, 2)); }
};

inline flatbuffers::Offset<Image> CreateImage(flatbuffers::FlatBufferBuilder &_fbb, uint32_t id, uint32_t maskId) {
  ImageBuilder builder_(_fbb);
  builder_.add_maskId(maskId);
  builder_.add_id(id);
  return builder_.Finish();
}

struct BlendMode : private flatbuffers::Table {
  int32_t mode() const { return GetField<int32_t>(4, 0); }
};

struct BlendModeBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_mode(int32_t mode) { fbb_.AddElement<int32_t>(4, mode, 0); }
  BlendModeBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<BlendMode> Finish() { return flatbuffers::Offset<BlendMode>(fbb_.EndTable(start_, 1)); }
};

inline flatbuffers::Offset<BlendMode> CreateBlendMode(flatbuffers::FlatBufferBuilder &_fbb, int32_t mode) {
  BlendModeBuilder builder_(_fbb);
  builder_.add_mode(mode);
  return builder_.Finish();
}

struct StrokeState : private flatbuffers::Table {
  int32_t refs() const { return GetField<int32_t>(4, 0); }
  uint8_t startCap() const { return GetField<uint8_t>(6, 0); }
  uint8_t dashCap() const { return GetField<uint8_t>(8, 0); }
  uint8_t endCap() const { return GetField<uint8_t>(10, 0); }
  float width() const { return GetField<float>(12, 0); }
  float miterLimit() const { return GetField<float>(14, 0); }
  float dashPhase() const { return GetField<float>(16, 0); }
  const flatbuffers::Vector<float> *dashList() const { return GetPointer<const flatbuffers::Vector<float> *>(18); }
};

struct StrokeStateBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_refs(int32_t refs) { fbb_.AddElement<int32_t>(4, refs, 0); }
  void add_startCap(uint8_t startCap) { fbb_.AddElement<uint8_t>(6, startCap, 0); }
  void add_dashCap(uint8_t dashCap) { fbb_.AddElement<uint8_t>(8, dashCap, 0); }
  void add_endCap(uint8_t endCap) { fbb_.AddElement<uint8_t>(10, endCap, 0); }
  void add_width(float width) { fbb_.AddElement<float>(12, width, 0); }
  void add_miterLimit(float miterLimit) { fbb_.AddElement<float>(14, miterLimit, 0); }
  void add_dashPhase(float dashPhase) { fbb_.AddElement<float>(16, dashPhase, 0); }
  void add_dashList(flatbuffers::Offset<flatbuffers::Vector<float>> dashList) { fbb_.AddOffset(18, dashList); }
  StrokeStateBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<StrokeState> Finish() { return flatbuffers::Offset<StrokeState>(fbb_.EndTable(start_, 8)); }
};

inline flatbuffers::Offset<StrokeState> CreateStrokeState(flatbuffers::FlatBufferBuilder &_fbb, int32_t refs, uint8_t startCap, uint8_t dashCap, uint8_t endCap, float width, float miterLimit, float dashPhase, flatbuffers::Offset<flatbuffers::Vector<float>> dashList) {
  StrokeStateBuilder builder_(_fbb);
  builder_.add_dashList(dashList);
  builder_.add_dashPhase(dashPhase);
  builder_.add_miterLimit(miterLimit);
  builder_.add_width(width);
  builder_.add_refs(refs);
  builder_.add_endCap(endCap);
  builder_.add_dashCap(dashCap);
  builder_.add_startCap(startCap);
  return builder_.Finish();
}

struct DisplayListNode : private flatbuffers::Table {
  uint8_t cmd() const { return GetField<uint8_t>(4, 0); }
  const Rect *rect() const { return GetStruct<const Rect *>(6); }
  uint8_t item_type() const { return GetField<uint8_t>(8, 0); }
  const void *item() const { return GetPointer<const void *>(10); }
  const StrokeState *strokeState() const { return GetPointer<const StrokeState *>(12); }
  int32_t flags() const { return GetField<int32_t>(14, 0); }
  const Matrix *ctm() const { return GetStruct<const Matrix *>(16); }
  float alpha() const { return GetField<float>(18, 0); }
  const flatbuffers::Vector<float> *color() const { return GetPointer<const flatbuffers::Vector<float> *>(20); }
};

struct DisplayListNodeBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_cmd(uint8_t cmd) { fbb_.AddElement<uint8_t>(4, cmd, 0); }
  void add_rect(const Rect *rect) { fbb_.AddStruct(6, rect); }
  void add_item_type(uint8_t item_type) { fbb_.AddElement<uint8_t>(8, item_type, 0); }
  void add_item(flatbuffers::Offset<void> item) { fbb_.AddOffset(10, item); }
  void add_strokeState(flatbuffers::Offset<StrokeState> strokeState) { fbb_.AddOffset(12, strokeState); }
  void add_flags(int32_t flags) { fbb_.AddElement<int32_t>(14, flags, 0); }
  void add_ctm(const Matrix *ctm) { fbb_.AddStruct(16, ctm); }
  void add_alpha(float alpha) { fbb_.AddElement<float>(18, alpha, 0); }
  void add_color(flatbuffers::Offset<flatbuffers::Vector<float>> color) { fbb_.AddOffset(20, color); }
  DisplayListNodeBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<DisplayListNode> Finish() { return flatbuffers::Offset<DisplayListNode>(fbb_.EndTable(start_, 9)); }
};

inline flatbuffers::Offset<DisplayListNode> CreateDisplayListNode(flatbuffers::FlatBufferBuilder &_fbb, uint8_t cmd, const Rect *rect, uint8_t item_type, flatbuffers::Offset<void> item, flatbuffers::Offset<StrokeState> strokeState, int32_t flags, const Matrix *ctm, float alpha, flatbuffers::Offset<flatbuffers::Vector<float>> color) {
  DisplayListNodeBuilder builder_(_fbb);
  builder_.add_color(color);
  builder_.add_alpha(alpha);
  builder_.add_ctm(ctm);
  builder_.add_flags(flags);
  builder_.add_strokeState(strokeState);
  builder_.add_item(item);
  builder_.add_rect(rect);
  builder_.add_item_type(item_type);
  builder_.add_cmd(cmd);
  return builder_.Finish();
}

struct DisplayList : private flatbuffers::Table {
  const flatbuffers::Vector<flatbuffers::Offset<DisplayListNode>> *nodes() const { return GetPointer<const flatbuffers::Vector<flatbuffers::Offset<DisplayListNode>> *>(4); }
};

struct DisplayListBuilder {
  flatbuffers::FlatBufferBuilder &fbb_;
  flatbuffers::uoffset_t start_;
  void add_nodes(flatbuffers::Offset<flatbuffers::Vector<flatbuffers::Offset<DisplayListNode>>> nodes) { fbb_.AddOffset(4, nodes); }
  DisplayListBuilder(flatbuffers::FlatBufferBuilder &_fbb) : fbb_(_fbb) { start_ = fbb_.StartTable(); }
  flatbuffers::Offset<DisplayList> Finish() { return flatbuffers::Offset<DisplayList>(fbb_.EndTable(start_, 1)); }
};

inline flatbuffers::Offset<DisplayList> CreateDisplayList(flatbuffers::FlatBufferBuilder &_fbb, flatbuffers::Offset<flatbuffers::Vector<flatbuffers::Offset<DisplayListNode>>> nodes) {
  DisplayListBuilder builder_(_fbb);
  builder_.add_nodes(nodes);
  return builder_.Finish();
}

inline const DisplayList *GetDisplayList(const void *buf) { return flatbuffers::GetRoot<DisplayList>(buf); }

}; // namespace mupdf
